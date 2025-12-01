package com.test.service;

import com.test.model.Certificate;
import java.util.List;

public interface CertificateRepository {
    List<Certificate> getAllCertificates();
}