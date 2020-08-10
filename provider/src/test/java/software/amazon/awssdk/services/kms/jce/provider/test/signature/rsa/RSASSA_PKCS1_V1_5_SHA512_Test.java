package software.amazon.awssdk.services.kms.jce.provider.test.signature.rsa;

import software.amazon.awssdk.services.kms.jce.provider.rsa.KmsRSAKeyFactory;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.provider.test.KeyIds;
import software.amazon.awssdk.services.kms.jce.provider.test.signature.KmsSignatureTest;

import java.security.KeyPair;

public class RSASSA_PKCS1_V1_5_SHA512_Test extends KmsSignatureTest {
    
    @Override
    protected KeyPair getKeyPair() {
        return KmsRSAKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_RSA);
    }

    @Override
    protected KmsSigningAlgorithm getKmsSigningAlgorithm() {
        return KmsSigningAlgorithm.RSASSA_PKCS1_V1_5_SHA_512;
    }

}
