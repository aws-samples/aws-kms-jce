package software.amazon.awssdk.services.kms.jce.provider.test.signature.ec;

import software.amazon.awssdk.services.kms.jce.provider.ec.KmsECKeyFactory;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.provider.test.KeyIds;
import software.amazon.awssdk.services.kms.jce.provider.test.signature.KmsSignatureTest;

import java.security.KeyPair;

public class ECDSA_SHA512_Test extends KmsSignatureTest {

    @Override
    protected KeyPair getKeyPair() {
        return KmsECKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_ECC_NIST_521);
    }

    @Override
    protected KmsSigningAlgorithm getKmsSigningAlgorithm() {
        return KmsSigningAlgorithm.ECDSA_SHA_512;
    }

}
