/*
 * Copyright 2013 User.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package MessagePackage;

import Krypter.Hasher;
import MessagePackage.Enums.MessageType;

/**
 *
 * @author User
 */
public class Message {

    private boolean rec = false;
    private final MessagePackage.Enums.MessageType TYPE;
    public final static String T_BENUTZER = "USER", T_HASHEDPW = "PASSWORD", T_AUTHKEY = "AUTHKEY", T_TYPE = "TYPE", T_HASH = "HASH", T_POINTS = "POINTS";
    public final String TRENN_PROPERTY = "\",", TRENN_VONBEZ = ":\"";
    private String[][] Werte;

    public Message(MessagePackage.Enums.MessageType TYPE, String benutzer, String hashedpasswort, boolean recieved) {
        this.TYPE = TYPE;
        this.rec = recieved;
        Werte = new String[3][2];
        Werte[0][0] = T_TYPE;
        Werte[0][1] = TYPE.name();
        Werte[1][0] = T_BENUTZER;
        Werte[1][1] = benutzer;
        Werte[2][0] = T_HASHEDPW;
        Werte[2][1] = hashedpasswort;

    }

    public Message(MessageType TYPE, String authkey, boolean recieved) {
        this.TYPE = TYPE;
        Werte = new String[2][2];
        Werte[0][0] = T_TYPE;
        Werte[0][1] = TYPE.name();
        Werte[1][0] = T_AUTHKEY;
        Werte[1][1] = authkey;
    }

    public Message(String input) {
        this.TYPE = initFromString(input);
    }

    public Message(MessageType TYPE) {
        this.TYPE = TYPE;
        Werte = new String[1][2];
        Werte[0][0] = T_TYPE;
        Werte[0][1] = this.TYPE.toString();
    }

    public String getMessageNHash() {
        String terg = getMessage();
        String erg;
        erg = terg + T_HASH + TRENN_VONBEZ;
        erg = erg + Hasher.ToMD5(terg) + TRENN_PROPERTY;
        return erg;
    }

    public String getMessage() {
        String erg;
        erg = T_TYPE + TRENN_VONBEZ + getMessageType().toString() + TRENN_PROPERTY;
        for (int i = 1; i < Werte.length; i++) {
            if (!Werte[i][0].equals(T_HASH)) {
                erg = erg + Werte[i][0].toUpperCase() + TRENN_VONBEZ + Werte[i][1] + TRENN_PROPERTY;
            }
        }
        return erg;
    }

    public void addString(String bez, String wert) {
        String[][] erg = new String[Werte.length + 1][2];
        for (int i = 0; i < erg.length - 2; i++) {
            erg[i][0] = Werte[i][0];
            erg[i][1] = Werte[i][1];
        }
        erg[erg.length - 1][0] = bez;
        erg[erg.length - 1][1] = wert;

        Werte = erg;
    }

    public void addInt(String bez, int wert) {
        String[][] erg = new String[Werte.length + 1][2];
        for (int i = 0; i < erg.length - 2; i++) {
            erg[i][0] = Werte[i][0];
            erg[i][1] = Werte[i][1];
        }
        erg[erg.length - 1][0] = bez;
        erg[erg.length - 1][1] = String.valueOf(wert);

        Werte = erg;
    }

    public String getString(String bez) {
        String erg = "";
        for (String[] Werte1 : Werte) {
            if (Werte1[0].equals(bez)) {
                erg = Werte1[1];
                break;
            }
        }
        return erg;
    }

    public String getString(int i) {
        return Werte[i][1];
    }

    private int getIndexofBezeichner(String bez) {
        int erg = -1;
        for (int i = 0; i < Werte.length; i++) {
            if (Werte[i][0].equals(bez)) {
                erg = i;
                break;
            }
        }
        return erg;
    }

    public void setString(String bez, String wert) {
        Werte[getIndexofBezeichner(bez.toUpperCase())][1] = wert;
    }

    public void setString(int index, String wert) {
        Werte[index][1] = wert;
    }

    public int getInt(String bez) {
        int erg = -1;
        for (String[] Werte1 : Werte) {
            if (Werte1[0].equals(bez)) {
                erg = Integer.parseInt(Werte1[1]);
                break;
            }
        }
        return erg;
    }

    public int getInt(int i) {
        return Integer.parseInt(Werte[i][1]);
    }

    public void setInt(String bez, int wert) {
        Werte[getIndexofBezeichner(bez)][1] = String.valueOf(wert);
    }

    public void setInt(int index, int wert) {
        Werte[index][1] = String.valueOf(wert);
    }

    public MessageType getMessageType() {
        return TYPE;
    }

    private MessageType initFromString(String message) {
        MessageType type = null;
        String[] sp = message.split(TRENN_PROPERTY);
        Werte = new String[sp.length][2];
        for (int i = 0; i < sp.length; i++) {
            String[] spt = sp[i].split(TRENN_VONBEZ);
            Werte[i][0] = spt[0].toUpperCase();
            Werte[i][1] = spt[1];
        }
        type = MessageType.valueOf(Werte[0][1]);
        setRecieved(true);
        return type;
    }

    public boolean isRecieved() {
        return rec;
    }

    public void setRecieved(boolean rec) {
        this.rec = rec;
    }

}
