package com.mathias.phonenest.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponse {
    private String responseCode;
    private String responseMessage;
}
