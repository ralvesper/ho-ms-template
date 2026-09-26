package com.highonline.exampleapi.core.ports.out.greeting;

/** Porta para o serviço externo de tradução. */
public interface ForTranslatingText {
    String translate(String text, String lang);
}
