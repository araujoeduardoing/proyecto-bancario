package liq_msa_bp_account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiErrorResponse {
    private int status;
    private String error;
    private String message;
    private String detailMessage;
    private String dateTimeException;

}
