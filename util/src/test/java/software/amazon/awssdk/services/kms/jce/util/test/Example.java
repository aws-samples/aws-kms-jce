package software.amazon.awssdk.services.kms.jce.util.test;

import software.amazon.awssdk.services.kms.jce.provider.KmsProvider;
import software.amazon.awssdk.services.kms.jce.provider.rsa.KmsRSAKeyFactory;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.util.crt.SelfSignedCrtGenerator;
import software.amazon.awssdk.services.kms.jce.util.csr.CsrGenerator;
import software.amazon.awssdk.services.kms.jce.util.csr.CsrInfo;
import software.amazon.awssdk.services.kms.KmsClient;

import java.security.KeyPair;
import java.security.Security;

/**
 * Example showing how to generate the CSR and the Self Signed Certificate.
 */
public class Example {

    public static void main(String[] args) {
        KmsClient kmsClient = KmsClient.builder().build();
        Security.addProvider(new KmsProvider(kmsClient));

        KeyPair keyPair = KmsRSAKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_RSA);
        KmsSigningAlgorithm kmsSigningAlgorithm = KmsSigningAlgorithm.RSASSA_PKCS1_V1_5_SHA_256;

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
    }

}
