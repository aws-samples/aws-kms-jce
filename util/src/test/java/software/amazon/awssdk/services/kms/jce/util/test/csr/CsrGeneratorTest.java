package software.amazon.awssdk.services.kms.jce.util.test.csr;

import software.amazon.awssdk.services.kms.jce.provider.KmsKey;
import software.amazon.awssdk.services.kms.jce.provider.KmsProvider;
import software.amazon.awssdk.services.kms.jce.provider.rsa.KmsRSAKeyFactory;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.util.csr.CsrGenerator;
import software.amazon.awssdk.services.kms.jce.util.csr.CsrInfo;
import software.amazon.awssdk.services.kms.jce.util.test.KeyIds;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.junit.Assert;
import org.junit.Test;
import software.amazon.awssdk.services.kms.KmsClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.Security;

public class CsrGeneratorTest {

    protected final KmsClient kmsClient;
    protected final KmsProvider kmsProvider;

    public CsrGeneratorTest() {
        this.kmsClient = KmsClient.builder().build();
        this.kmsProvider = new KmsProvider(kmsClient);
        Security.insertProviderAt(this.kmsProvider, Security.getProviders().length);
    }

    @Test
    public void test() throws IOException, OperatorCreationException, PKCSException {
        KeyPair keyPair = KmsRSAKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_RSA);
        String keyId = ((KmsKey)keyPair.getPrivate()).getId();
        KmsSigningAlgorithm kmsSigningAlgorithm = KmsSigningAlgorithm.RSASSA_PKCS1_V1_5_SHA_256;

        System.out.println("----------------------------------------------");
        System.out.println(String.format("Generating CSR for Key [ %s ] with Algorithm [ %s ]", keyId, kmsSigningAlgorithm.name()));
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

        String csr = CsrGenerator.generate(keyPair, csrInfo, kmsSigningAlgorithm);
        System.out.println("CSR:");
        System.out.println(csr);

        PEMParser pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(csr.getBytes())));
        PKCS10CertificationRequest request = (PKCS10CertificationRequest) pemParser.readObject();

        ContentVerifierProvider contentVerifierProvider = new JcaContentVerifierProviderBuilder().build(keyPair.getPublic());
        Assert.assertTrue(request.isSignatureValid(contentVerifierProvider));

    }

}
