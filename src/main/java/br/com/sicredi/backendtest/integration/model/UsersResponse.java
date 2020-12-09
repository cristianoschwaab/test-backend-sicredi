package br.com.sicredi.backendtest.integration.model;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

public class UsersResponse implements Serializable {

    public enum CpfStatus {
        ABLE_TO_VOTE, UNABLE_TO_VOTE;
    }

    private CpfStatus status;

    public UsersResponse() {
        super();
    }

    public UsersResponse(CpfStatus status) {
        this();
        this.status = status;
    }

    public CpfStatus getStatus() {
        return status;
    }

    @Transient
    public Boolean isUnableToVote() {
        return CpfStatus.UNABLE_TO_VOTE.equals(this.status);
    }

    @Transient
    public Boolean isAbleToVote() {
        return CpfStatus.ABLE_TO_VOTE.equals(this.status);
    }

}
