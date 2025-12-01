package com.test.testing.doubles;

import com.test.model.Certificate;
import com.test.service.CertificateRepository;
import java.util.List;

public class CertificateRepositoryStub implements CertificateRepository {
    private final List<Certificate> certificates;

    public CertificateRepositoryStub(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return certificates;
    }
}