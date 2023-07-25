package software.amazon.awssdk.services.kms.jce.provider.rsa;

import software.amazon.awssdk.services.kms.jce.provider.KmsKey;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class KmsRSAPrivateKey implements KmsKey, RSAPrivateKey {

    private static final long serialVersionUID = 1L;
	private final String id;
    private final String algorithm = "RSA";
    private final String format = "X.509";

    public KmsRSAPrivateKey(String id) {
    	this.id = id;
    }
    
    @Override
    public BigInteger getPrivateExponent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigInteger getModulus() {
        throw new UnsupportedOperationException();
    }

    @Override
	public String getId() {
		return id;
	}

    @Override
	public String getAlgorithm() {
		return algorithm;
	}

    @Override
	public String getFormat() {
		return format;
	}

}
