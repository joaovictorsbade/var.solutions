package com.br.var.solutions.application.services.impl;

import com.br.var.solutions.application.services.use.MundialUseCase;

public class MundialUseCaseImpl implements MundialUseCase {

    public String calculoMundial(String time){
        return calculaMundial(time);
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
}
