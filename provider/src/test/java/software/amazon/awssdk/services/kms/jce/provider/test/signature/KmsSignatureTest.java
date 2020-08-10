package software.amazon.awssdk.services.kms.jce.provider.test.signature;

import software.amazon.awssdk.services.kms.jce.provider.KmsKey;
import software.amazon.awssdk.services.kms.jce.provider.KmsProvider;
import software.amazon.awssdk.services.kms.jce.provider.KmsPublicKey;
import software.amazon.awssdk.services.kms.jce.provider.signature.KmsSigningAlgorithm;
import software.amazon.awssdk.services.kms.jce.provider.test.util.StringUtil;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import software.amazon.awssdk.services.kms.KmsClient;

import java.security.KeyPair;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;

public abstract class KmsSignatureTest {

    protected final KmsClient kmsClient;
    protected final KmsProvider kmsProvider;

    public KmsSignatureTest() {
        this.kmsClient = KmsClient.builder().build();
        this.kmsProvider = new KmsProvider(kmsClient);
    }

    protected abstract KeyPair getKeyPair();

    protected abstract KmsSigningAlgorithm getKmsSigningAlgorithm();

    protected String getSigningAlgorithm() {
        return getKmsSigningAlgorithm().getAlgorithm();
    }

    protected AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return null;
    }

    @Test
    @SneakyThrows
    public void test() {
        KeyPair keyPair = getKeyPair();
        KmsSigningAlgorithm kmsSigningAlgorithm = getKmsSigningAlgorithm();

        String keyId = ((KmsKey) keyPair.getPrivate()).getId();

        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println(String.format("Testing Key [ %s ] with Algorithm [ %s ]", keyId, kmsSigningAlgorithm.name()));
        System.out.println("----------------------------------------------");

        String message = "text to be signed!";

        Signature kmsSignature = Signature.getInstance(kmsSigningAlgorithm.getAlgorithm(), kmsProvider);
        kmsSignature.initSign(keyPair.getPrivate());
        kmsSignature.update(message.getBytes());
        byte[] signatureBytes = kmsSignature.sign();

        System.out.println(String.format("Signature: %s", StringUtil.hex(signatureBytes)));

        kmsSignature.initVerify(keyPair.getPublic());
        kmsSignature.update(message.getBytes());
        boolean valid = kmsSignature.verify(signatureBytes);

        System.out.println(String.format("Verification via KMS: %s", valid));
        Assert.assertTrue("Verification via KMS failed!", valid);

        Signature defaultSignature = Signature.getInstance(getSigningAlgorithm());
        AlgorithmParameterSpec algorithmParameterSpec = getAlgorithmParameterSpec();
        if (algorithmParameterSpec != null) {
            defaultSignature.setParameter(algorithmParameterSpec);
        }
        defaultSignature.initVerify(((KmsPublicKey) keyPair.getPublic()).getPublicKey());
        defaultSignature.update(message.getBytes());
        valid = defaultSignature.verify(signatureBytes);

        System.out.println(String.format("Verification via Default Provider: %s", valid));
        Assert.assertTrue("Verification via Default Provider failed!", valid);
    }

}
