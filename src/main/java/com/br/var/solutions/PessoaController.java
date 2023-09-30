package com.br.var.solutions;

import ch.qos.logback.core.net.server.Client;
import com.br.var.solutions.security.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStore;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin(origins = "*")
@Slf4j
public class PessoaController {

    private static final String SECRET_KEY = "tokenApiVarSolutions2023RrequestAurhorizationbRASIL";

    @GetMapping
    public ResponseEntity<Object> get() {
        PessoaRequest pessoaRequest1 = new PessoaRequest();
        pessoaRequest1.setNome("Joao");
        pessoaRequest1.setSobrenome("Barbosa");
        pessoaRequest1.setEndereco("Rua Joaquin Santos Leite");
        pessoaRequest1.setIdade(17);

        return ResponseEntity.ok(pessoaRequest1);
    }

    @GetMapping("/resumo")
    public ResponseEntity<Object> getPessoa(@RequestBody PessoaRequest pessoinha, @RequestParam(value = "valida_mundial") Boolean DesejavalidaMundial) {
        InformacoesIMC imc = new InformacoesIMC();
        int anoNascimento = 0;
        String impostoRenda = null;
        String saldoEmDolar = null;
        String validaMundial = null;

        if (!pessoinha.getNome().isEmpty()) {

            log.info("Iniciando processo de resumo da pessoa: ", pessoinha);

            if (Objects.nonNull(pessoinha.getPeso()) && Objects.nonNull(pessoinha.getAltura())) {
                log.info("Iniciando o processo de cálculo do IMC");
                imc = calculaImc(pessoinha.getPeso(), pessoinha.getAltura());
            }

            if (Objects.nonNull(pessoinha.getIdade())) {
                log.info("Iniciando o cálculo do ano de nascimento");
                anoNascimento = calculaAnoNascimento(pessoinha.getIdade());
            }


            if (Objects.nonNull(pessoinha.getSalario())) {
                log.info("Iniciando o cálculo do imposto de renda");
                impostoRenda = calculoFaixaImpostoRenda(pessoinha.getSalario());
            }

            if (Boolean.TRUE.equals(DesejavalidaMundial)) {
                if (Objects.nonNull(pessoinha.getTime())) {
                    log.info("Validando se o time de coração tem mundial: ");
                    validaMundial = calculaMundial(pessoinha.getTime());
                }
            }

            if (Objects.nonNull(pessoinha.getSaldo())) {
                log.info("Converter real em dolar");
                saldoEmDolar = converteSaldoEmDolar(pessoinha.getSaldo());
            }

            log.info("Montando objeto de retorno para o front-end");
            Object resumo = montarRespostaFrontEnd(pessoinha, imc, anoNascimento, impostoRenda, validaMundial, saldoEmDolar);


            return ResponseEntity.ok(resumo);
        }
        return ResponseEntity.noContent().build();
    }

    private PessoaResponse montarRespostaFrontEnd(PessoaRequest pessoa, InformacoesIMC imc, int anoNascimento, String impostoRenda, String validaMundial, String saldoEmDolar) {

        PessoaResponse response = new PessoaResponse();

        response.setNome(pessoa.getNome());
        response.setImc(imc.getImc());
        response.setClassificacaoIMC(imc.getClassificacao());
        response.setSalario(impostoRenda);
        response.setAnoNascimento(anoNascimento);
        response.setMundialClubes(validaMundial);
        response.setSaldoEmDolar(saldoEmDolar);
        response.setIdade(pessoa.getIdade());
        response.setTime(pessoa.getTime());
        response.setSobrenome(pessoa.getSobrenome());
        response.setEndereco(pessoa.getEndereco());
        response.setAltura(pessoa.getAltura());
        response.setPeso(pessoa.getPeso());
        response.setSaldo(pessoa.getSaldo());

        return response;
    }

    private String converteSaldoEmDolar(double saldo) {
        return String.valueOf(saldo / 4.49);
    }

    private String calculaMundial(String time) {
        if (time.equalsIgnoreCase("Corinthians")) {
            return "Parabéns, o seu time possui dois mundiais de clubes conforme a FIFA";
        } else if (time.equalsIgnoreCase("São Paulo")) {
            return "Parabéns, o seu time possui Três mundiais de clubes conforme a FIFA";
        } else if (time.equalsIgnoreCase("Santos")) {
            return "Parabéns, o seu time possui dois mundiais de clubes conforme a FIFA";
        } else {
            return "Poxa, que pena, continue torcendo para seu time ganhar um mundial";
        }
    }

    private String calculoFaixaImpostoRenda(double salario) {
        log.info("Iniciando o cálculo do imposto de renda");
        String novoSalarioCalculado;


        if (salario < 1903.98) {
            return "isento";

        } else if (salario > 1903.98 && salario < 2826.65) {
            double calculoIRF = 142.80 - ((salario * 0.075) / 100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);

            return novoSalarioCalculado;

        } else if (salario > 2826.65 && salario < 3751.05) {
            double calculoIRF = 354.80 - ((salario * 0.15) / 100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);

            return novoSalarioCalculado;

        } else if (salario > 3751.05 && salario < 4664.68) {
            double calculoIRF = 636.13 - ((salario * 0.225) / 100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);

            return novoSalarioCalculado;

        } else {
            double calculoIRF = 869.36 - ((salario * 275) / 100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);

            return novoSalarioCalculado;
        }
    }

    private int calculaAnoNascimento(int idade) {
        LocalDate datalocal = LocalDate.now();
        int anoAtual = datalocal.getYear();
        return anoAtual - idade;
    }

    private InformacoesIMC calculaImc(double peso, double altura) {
        double imc = peso / (altura * altura);

        InformacoesIMC imcCalculado = new InformacoesIMC();

        if (imc < 18.5) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("abaixo do peso");
            return imcCalculado;
        } else if (imc >= 18.5 && imc <= 24.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("peso ideal");
            return imcCalculado;
        } else if (imc > 24.9 && imc <= 29.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("excesso de peso");
            return imcCalculado;
        } else if (imc > 29.9 && imc <= 34.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("obesidade I");
            return imcCalculado;
        } else if (imc > 34.9 && imc < 39.9) {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("obesidade II");
            return imcCalculado;
        } else {
            imcCalculado.setImc(String.valueOf(imc));
            imcCalculado.setClassificacao("obesidade III");
            return imcCalculado;
        }
    }
//    @DeleteMapping
//    public void retornDelete(){
//
//    }
//    @PutMapping
//    public void retornoPut(){
//
//    }
//    @PostMapping
//    public void retornoPost(){
//
//    }

    @PostMapping(path = "/authorization", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<GenerateToken> authorization(@RequestParam("client_id") String clientId, @RequestParam("password") String password) {
        log.info("Iniciando a tentativa de geração de token para o usuario" + clientId);
        Boolean validaUsuario = ValidaUsuario.validaUsuario(clientId, password);
        if (Boolean.FALSE.equals(validaUsuario)) {
            log.error("Não foi possivel gerar o token, pois o usuario ou a senha estão incorretos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenerateToken());
        }

        long expirationDate = System.currentTimeMillis() + 1800000;

        String token = Jwts.builder()
                .setSubject(clientId)
                .setExpiration(new Date(expirationDate))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        GenerateToken tokenResponse = new GenerateToken();

        tokenResponse.setToken(token);
        tokenResponse.setExpiraEm(new Date(expirationDate));
        tokenResponse.setTempoValidacao(expirationDate);

        log.info("Token Gerado com sucesso para o usuario " + clientId + " Em: " + System.currentTimeMillis());
        return ResponseEntity.ok(tokenResponse);

    }

    @PostMapping(path = "/authenticate", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<GenerateToken> generateToken(@RequestParam("client_id") String clientId, @RequestParam("password") String password) {
        log.info("Iniciando a tentativa de geração de token para o usuario" + clientId);
        Boolean validaUsuario = ValidaUsuario.validaUsuario(clientId, password);
        if (Boolean.FALSE.equals(validaUsuario)) {
            log.error("Não foi possivel gerar o token, pois o usuario ou a senha estão incorretos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenerateToken());
        }

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.generateToken(clientId);


        GenerateToken tokenResponse = new GenerateToken();
        tokenResponse.setToken(token);

        log.info("Token Gerado com sucesso para o usuario " + clientId + " Em: " + System.currentTimeMillis());
        return ResponseEntity.ok(tokenResponse);

    }
}
