package com.qtivate.server.api;

import com.qtivate.server.exceptions.ExpiredTokenException;
import com.qtivate.server.exceptions.InvalidTokenException;
import com.qtivate.server.model.SimpleStudent;
import com.qtivate.server.model.StudentPresence;
import com.qtivate.server.service.DeviceService;
import com.qtivate.server.service.SubjectService;
import com.qtivate.server.service.TokenGenerator;
import com.qtivate.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Classe PresenceController
 * É a Classe que possui os Endpoints da API na parte de presença.
 * Tudo relacionado à presença é encontrado aqui.
 * <p>
 * Endpoints:
 * registerPresence — Registra a presença de um aluno dado um token,
 * o seu RA (aid) e o seu Mac Address.
 * registerPresencesByClassId — Registra a presença de multiplos alunos
 * dado um token.
 * getPresencesByClass — Dado um classId, retorna a presença de todos
 * os alunos daquela aula.
 * getPresenceByAID — Dado um RA (aid), retorna a presença daquele aluno
 * em todas as aulas.
 * generateAllQrCodes — Dado classesIds e a quantidade desejada, gera uma
 * quantidade fornecida de tokens e os retorna.
 *
 * @author Leonardo Hana Bersello
 * @author Ariane Paula Barros
 * @version 0.5
 */
@CrossOrigin(origins = {"http://localhost:3000","https://localhost:3000"})
@RequestMapping("api/v1/presence")
@RestController
public class PresenceController {
    private final SubjectService subjectService;
    private final TokenService tokenService;
    private final DeviceService deviceService;

    @Autowired
    public PresenceController(SubjectService subjectService, TokenService tokenService, DeviceService deviceService) {
        this.subjectService = subjectService;
        this.tokenService = tokenService;
        this.deviceService = deviceService;
    }

    /**
     * Registra a presença de um aluno baseado no seu RA (aid) por um
     * token. O MAC Address do seu dispositivo também é recebido para ficar
     * registrado no banco e deixe-o bloqueado por um tempo.
     * @param token Token do QR Code escaneado, que contém as aulas
     * @param aid RA do aluno
     * @param mac MAC Address do dispositivo o qual chamou o método
     * @return Resposta HTTP
     */
    @PostMapping(headers = {"aid", "token", "mac"})
    public ResponseEntity<String> registerPresence(@RequestHeader String token, @RequestHeader String aid, @RequestHeader String mac) {
        try {
            if(!subjectService.isTokenValid(token)) return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Token expirado");

            int macVerification = deviceService.verifyMac(mac);
            if (macVerification == 1) deviceService.addDevice(mac);
            if (macVerification == 2) return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Device " + mac + " is still blocked");

            String[] data = token.split(":");
            String[] classes = data[1].split(",");
            for(int i = 0; i < classes.length; i++) {
                int code = subjectService.addInPresenceByClassId(classes[i], aid);
                if (code == 1) return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Class " + i + " not found");
                if (code == 2) return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Duplicated presence in class " + i);
                if (code == 3) return ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE)
                        .body("Student does not belong to class " + i);
            }
        } catch (ExpiredTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (InvalidTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }catch (Exception error) {
            System.err.println(error.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aid);
    }

    /**
     * Registra a presença de uma lista de alunos por seus RAs por um token.
     * @param token Token do QR Code escaneado, que contém as aulas
     * @param aids ‘String’ com todos os RAs separados por ","
     * @return Resposta HTTP
     */
    @PostMapping(headers = {"token", "aids"})
    public ResponseEntity<String> registerPresencesByClassId(@RequestHeader String token, @RequestHeader String aids) {
        List<String> successes = new ArrayList<>();
        try {
            if(!subjectService.isTokenValid(token)) return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Token expirado");
            if (aids.length() == 0) return ResponseEntity.ok().build();

            String[] data = token.split(":");
            String[] classes = data[1].split(",");
            String[] list_aids = aids.split(",");

            if (classes.length < 1) return ResponseEntity.badRequest().build();
            for(int i = 0; i < classes.length; i++) {
                Map<String, String> result = subjectService.setPresenceByClassId(classes[i], List.of(list_aids));
                return ResponseEntity.ok().body(
                        "Adicionamos: " + result.get("added") +
                                "\n Removidos: " + result.get("removed")
                );
            }
        } catch (ExpiredTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (InvalidTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }catch (Exception error) {
            System.err.println(error.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error.getMessage());
        }
        return ResponseEntity
                .badRequest()
                .build();
    }

    /**
     * Retorna a Presença de todos os alunos de um classId.
     * @param classId ID da Aula
     * @return Resposta HTTP contendo a lista
     */
    @GetMapping(headers = "classId")

    public ResponseEntity<List<SimpleStudent>> getPresencesByClass(@RequestHeader String classId) {
        return ResponseEntity.ok().body(subjectService.getPresentsByClassId(classId));
    }

    /**
     * Retorna a porcentagem de presença de todas as aulas de um aluno.
     * @param aid RA do aluno
     * @return Resposta HTTP contendo as porcentagens
     */
    @GetMapping(headers = "aid")
    public ResponseEntity<List<StudentPresence>> getPresenceByAID(@RequestHeader String aid) {
        try {
            List<StudentPresence> presences = subjectService.getPresenceByAID(aid);
            if (presences.isEmpty()) return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
            return ResponseEntity
                    .ok()
                    .body(presences);
        } catch (Exception error) {
            System.err.println(error.getMessage());
            throw error;
        }
    }

    /**
     * Gera um número fornecido de tokens com base nos classId fornecidos.
     * @param classes IDs das Aulas (classId)
     * @param quantity Número de tokens a serem gerados
     * @return Resposta HTTP
     */
    @PostMapping(headers = {"classes", "quantity"})
    public ResponseEntity<List<String>> generateAllQrCodes(@RequestHeader String classes, @RequestHeader int quantity) {
        try {
            if (quantity < 1) throw new Exception("Quantidade de tokens inválida");
            String[] tokens = new String[quantity];
            for (int i = 0; i < quantity; i++)
                tokens[i] = TokenGenerator.generateToken(classes);
            tokenService.addManyTokens(tokens);
            String[] classesIds = classes.split(",");
            for(String classId : classesIds) subjectService.addTokensByClassId(classId, tokens);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(List.of(tokens));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    @GetMapping(headers = "subId")
    public ResponseEntity<Object[]> getStudentsByClassId(@RequestHeader String subId) {
        return ResponseEntity.ok().body(subjectService.getAllStudentsByClassId(subId).toArray());
    }

}
