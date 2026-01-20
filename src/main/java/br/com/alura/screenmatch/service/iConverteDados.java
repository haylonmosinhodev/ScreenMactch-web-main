package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface iConverteDados {
    <T> T obterDados(String json, Class<T> classe) throws JsonProcessingException;
}
