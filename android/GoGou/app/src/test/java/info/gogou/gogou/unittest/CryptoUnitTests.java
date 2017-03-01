package info.gogou.gogou.unittest;


import android.util.Base64;
import android.util.Log;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;
import org.junit.Test;

import info.gogou.gogou.constants.GoGouConstants;

/**
 * Created by Letian_Xu on 6/27/2016.
 */
public class CryptoUnitTests {

    @Test
    public final void crytoTest() {
        JNCryptor cryptor = new AES256JNCryptor();
        byte[] plaintext = "aa123456".getBytes();

        try {
            byte[] ciphertext = cryptor.encryptData(plaintext, GoGouConstants.SEED_VALUE.toCharArray());
            String encryptedPassword = Base64.encodeToString(ciphertext, Base64.NO_WRAP);
            Log.d("CryptoUnitTests", "Encrypted password is: " + encryptedPassword);
        } catch (CryptorException e) {
            // Something went wrong
            e.printStackTrace();
        }

    }

}
