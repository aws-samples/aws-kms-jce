package software.amazon.awssdk.services.kms.jce.provider.test.signature.rsa;

import software.amazon.awssdk.services.kms.jce.provider.rsa.KmsRSAKeyFactory;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.provider.test.KeyIds;
import software.amazon.awssdk.services.kms.jce.provider.test.signature.KmsSignatureTest;

import java.security.KeyPair;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

public class RSASSA_PSS_SHA256_Test extends KmsSignatureTest {

    @Override
    protected KeyPair getKeyPair() {
        return KmsRSAKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_RSA);
    }

    @Override
    protected KmsSigningAlgorithm getKmsSigningAlgorithm() {
        return KmsSigningAlgorithm.RSASSA_PSS_SHA_256;
    }

    @Override
    protected String getSigningAlgorithm() {
        return "RSASSA-PSS";
    }

    @Override
    protected AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
    }
}
