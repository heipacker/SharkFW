package net.sharkfw.asip.engine;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.SpatialSemanticTag;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.protocols.StreamConnection;
import net.sharkfw.system.Base64;
import net.sharkfw.system.L;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Signature;

/**
 * Objects of this class are produced by the framework in order
 * to be serialized and transmitted to another peer.
 *
 * @author thsc
 */
public class ASIPOutMessage extends ASIPMessage {

    private Writer osw = null;

    public ASIPOutMessage(SharkEngine engine,
                          StreamConnection connection,
                          long ttl,
                          PeerSemanticTag sender,
                          PeerSemanticTag receiverPeer,
                          SpatialSemanticTag receiverSpatial,
                          TimeSemanticTag receiverTime) throws SharkKBException {

        super(engine, connection, ttl, sender, receiverPeer, receiverSpatial, receiverTime);

        osw = new OutputStreamWriter(connection.getOutputStream().getOutputStream(), StandardCharsets.UTF_8);
    }

    public void expose(ASIPInterest interest) {
        this.setCommand(ASIPMessage.ASIP_EXPOSE);

//        this.initSecurity();

        try {
            this.osw.write(ASIPSerializer.serializeExpose(this, interest).toString());
            this.osw.close();
        } catch (SharkKBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void insert(ASIPKnowledge knowledge) {

        this.setCommand(ASIPMessage.ASIP_INSERT);

//        this.initSecurity();

        try {
            this.osw.write(ASIPSerializer.serializeInsert(this, knowledge).toString());
            this.osw.close();
        } catch (SharkKBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void raw(byte[] raw) {

        this.setCommand(ASIPMessage.ASIP_RAW);

//        this.initSecurity();

        try {
            this.osw.write(ASIPSerializer.serializeRaw(this, raw).toString());
            this.osw.close();
        } catch (SharkKBException e) {
            L.d("Serialize failed");
            e.printStackTrace();
        } catch (IOException e) {
            L.d("Write failed");
            e.printStackTrace();
        }
    }

}