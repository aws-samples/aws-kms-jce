package software.amazon.awssdk.services.kms.jce.provider.ec;

import software.amazon.awssdk.services.kms.jce.provider.KmsKey;

import java.math.BigInteger;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;

public class KmsECPrivateKey implements KmsKey, ECPrivateKey {

    private static final long serialVersionUID = 1L;
	private final String id;
    private final String algorithm = "EC";
    private final String format = "PKCS#8";

    public KmsECPrivateKey(String id) {
    	this.id = id;
    }
    
    @Override
    public BigInteger getS() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ECParameterSpec getParams() {
        throw new UnsupportedOperationException();
    }

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
