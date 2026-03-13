package com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
