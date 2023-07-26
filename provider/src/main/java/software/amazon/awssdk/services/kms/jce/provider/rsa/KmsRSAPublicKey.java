package software.amazon.awssdk.services.kms.jce.provider.rsa;

import software.amazon.awssdk.services.kms.jce.provider.KmsPublicKey;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public class KmsRSAPublicKey implements KmsPublicKey, RSAPublicKey {

    private static final long serialVersionUID = 1L;
	private final String id;
    private final RSAPublicKey publicKey;
    
    public KmsRSAPublicKey(String id, RSAPublicKey publicKey) {
    	this.id = id;
    	this.publicKey = publicKey;
    }

    @Override
    public BigInteger getPublicExponent() {
        return publicKey.getPublicExponent();
    }

    @Override
    public String getAlgorithm() {
        return publicKey.getAlgorithm();
    }

    @Override
    public String getFormat() {
        return publicKey.getFormat();
    }

    @Override
    public byte[] getEncoded() {
        return publicKey.getEncoded();
    }

    @Override
    public BigInteger getModulus() {
        return publicKey.getModulus();
    }

    @Override
	public String getId() {
		return id;
	}

    @Override
	public PublicKey getPublicKey() {
		return publicKey;
	}
}
