## Gerar token
    http://localhost:8080/api/v1/presence
    Adicionar header chamado classes com valor no estilo 12345P-SFW-22-2-001,12345P-SFW-22-2-002
    Ids de cada aula separado por vírgula
    A resposta do endpoint é uma string no formato puc-12345P-SFW-22-2-001,12345P-SFW-22-2-002-tokenstring
    
    No endpoint http://localhost:8080/api/v1/presence
    Adicionar header chamado token cujo valor será o retorno do primeiro endpoint
    Erros relacionados ao token são: mal formatado (não está no formato puc:array de string:string) ou token inválido (expirado - 10min)
