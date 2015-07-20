package com.googlecode.common.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class StatusExposingServletResponse extends HttpServletResponseWrapper {

    private int httpStatus = SC_OK;

    public StatusExposingServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(final int sc) throws IOException {
        super.sendError(sc);

        httpStatus = sc;
    }

    @Override
    public void sendError(final int sc, final String msg) throws IOException {
        super.sendError(sc, msg);

        httpStatus = sc;
    }

    @Override
    public void sendRedirect(final String location) throws IOException {
        super.sendRedirect(location);

        httpStatus = SC_MOVED_TEMPORARILY;
    }

    @Override
    public void reset() {
        super.reset();

        httpStatus = SC_OK;
    }

    @Override
    public void setStatus(final int sc) {
        super.setStatus(sc);

        httpStatus = sc;
    }

    @Override
    public void setStatus(int status, String string) {
        super.setStatus(status, string);

        httpStatus = status;
    }

    public int getStatus() {
        return httpStatus;
    }
}
