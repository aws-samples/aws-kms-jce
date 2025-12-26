package software.amazon.awssdk.services.kms.jce.provider.test;

import software.amazon.awssdk.services.kms.jce.provider.KmsKey;
import software.amazon.awssdk.services.kms.jce.provider.KmsProvider;
import org.junit.Assert;
import org.junit.Test;
import software.amazon.awssdk.services.kms.KmsClient;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Enumeration;
import java.util.UUID;

public class KmsKeyStoreTest {

    protected final KmsClient kmsClient;
    protected final KmsProvider kmsProvider;

    public KmsKeyStoreTest() {
        this.kmsClient = KmsClient.builder().build();
        this.kmsProvider = new KmsProvider(kmsClient);
    }

    @Test
    public void testAliases() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        Enumeration<String> aliases = keyStore.aliases();
        Assert.assertTrue(aliases.hasMoreElements());
    }

    @Test
    public void testContainsAlias() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        boolean contains = keyStore.containsAlias(KeyIds.SIGN_RSA_4096_ALIAS);
        Assert.assertTrue(contains);
    }

    @Test
    public void testNotContainsAlias() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        boolean contains = keyStore.containsAlias(UUID.randomUUID().toString());
        Assert.assertFalse(contains);
    }

    @Test
    public void testSize() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        int size = keyStore.size();
        Assert.assertTrue(size > 0);
    }

    @Test
    public void testGetRsaKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        Key key = keyStore.getKey(KeyIds.SIGN_RSA_4096_ALIAS, null);
        Assert.assertNotNull(key);
        Assert.assertTrue(key instanceof KmsKey);
        Assert.assertTrue(key instanceof RSAPrivateKey);
    }

    @Test
    public void testGetEcKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        Key key = keyStore.getKey(KeyIds.SIGN_ECC_NIST_521_ALIAS, null);
        Assert.assertNotNull(key);
        Assert.assertTrue(key instanceof KmsKey);
        Assert.assertTrue(key instanceof ECPrivateKey);
    }

    @Test
    public void testGetEncryptKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("KMS", this.kmsProvider);
        keyStore.load(null, null);

        try {
            keyStore.getKey(KeyIds.RSA_ENCRYPT_ALIAS, null);
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().startsWith("Unsupported Key type"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
