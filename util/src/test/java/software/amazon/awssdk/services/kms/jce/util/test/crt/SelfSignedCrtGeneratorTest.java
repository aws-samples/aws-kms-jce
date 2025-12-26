package software.amazon.awssdk.services.kms.jce.util.test.crt;

import software.amazon.awssdk.services.kms.jce.provider.KmsKey;
import software.amazon.awssdk.services.kms.jce.provider.KmsProvider;
import software.amazon.awssdk.services.kms.jce.provider.rsa.KmsRSAKeyFactory;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.util.crt.SelfSignedCrtGenerator;
import software.amazon.awssdk.services.kms.jce.util.csr.CsrGenerator;
import software.amazon.awssdk.services.kms.jce.util.csr.CsrInfo;
import software.amazon.awssdk.services.kms.jce.util.test.KeyIds;
import org.junit.Test;
import software.amazon.awssdk.services.kms.KmsClient;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SelfSignedCrtGeneratorTest {

    protected final KmsClient kmsClient;
    protected final KmsProvider kmsProvider;

    public SelfSignedCrtGeneratorTest() {
        this.kmsClient = KmsClient.builder().build();
        this.kmsProvider = new KmsProvider(kmsClient);
        Security.insertProviderAt(this.kmsProvider, Security.getProviders().length);
    }

    @Test
    public void test() throws CertificateException {
        KeyPair keyPair = KmsRSAKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_RSA);
        String keyId = ((KmsKey)keyPair.getPrivate()).getId();
        KmsSigningAlgorithm kmsSigningAlgorithm = KmsSigningAlgorithm.RSASSA_PKCS1_V1_5_SHA_256;

        System.out.println("----------------------------------------------");
        System.out.println(String.format("Generating Self Signed Crt for Key [ %s ] with Algorithm [ %s ]", keyId, kmsSigningAlgorithm.name()));
        System.out.println("----------------------------------------------");

        CsrInfo csrInfo = CsrInfo.builder()
                .cn("kms.aws.amazon.com")
                .ou("AWS")
                .o("Amazon")
                .l("Sao Paulo")
                .st("Sao Paulo")
                .c("BR")
                .mail("kms@amazon.com")
                .build();

        System.out.println("CSR Info: " + csrInfo.toString());
        System.out.println();

        String csr = CsrGenerator.generate(keyPair, csrInfo, kmsSigningAlgorithm);
        System.out.println("CSR:");
        System.out.println(csr);

        String crt = SelfSignedCrtGenerator.generate(keyPair, csr, kmsSigningAlgorithm, 365);
        System.out.println("CRT:");
        System.out.println(crt);

        X509Certificate certificate = getCertificate(crt);
        certificate.checkValidity();
    }

    private X509Certificate getCertificate(String certificate) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(certificate.getBytes(StandardCharsets.UTF_8));
        return (X509Certificate) certificateFactory.generateCertificate(inputStream);
    }

}
