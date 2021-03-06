package net.sharkfw.knowledgeBase.sync;

import net.sharkfw.knowledgeBase.PeerSTSet;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.knowledgeBase.sync.manager.SyncManager;
import net.sharkfw.peer.J2SEAndroidSharkEngine;
import net.sharkfw.system.L;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by j4rvis on 19.09.16.
 */
public class SyncManagerTest {

    @Test
    public void groupInvitationTest(){

        L.setLogLevel(L.LOGLEVEL_ALL);

        // Basics
        J2SEAndroidSharkEngine aliceEngine = new J2SEAndroidSharkEngine();
        SyncManager aliceManager = aliceEngine.getSyncManager();
        aliceManager.allowInvitation(true);

        J2SEAndroidSharkEngine bobEngine = new J2SEAndroidSharkEngine();
        SyncManager bobManager = bobEngine.getSyncManager();
        bobManager.allowInvitation(true);

        // Create alice
        PeerSemanticTag alice = InMemoSharkKB.createInMemoPeerSemanticTag("alice", "alice.de", "tcp://localhost:7070");
        aliceEngine.setEngineOwnerPeer(alice);
//        try {
//            aliceEngine.startTCP(7070);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Create bob
        PeerSemanticTag bob = InMemoSharkKB.createInMemoPeerSemanticTag("bob", "bob.de", "tcp://localhost:7071");
        bobEngine.setEngineOwnerPeer(bob);
        try {
            bobEngine.startTCP(7071);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a kb to share
        InMemoSharkKB sharkKB = new InMemoSharkKB();
        try {
            sharkKB.addInformation("This is an information", InMemoSharkKB.createInMemoASIPInterest());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        PeerSTSet peerSTSet = sharkKB.createInMemoPeerSTSet();
        try {
            peerSTSet.merge(bob);
//            peerSTSet.merge(alice);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        SemanticTag kbName = sharkKB.createInMemoSemanticTag("kbName", "kbsi.de");

        // Now create the component

        SyncComponent component = aliceManager.createSyncComponent(sharkKB, kbName, peerSTSet, alice, true);
        try {
            component.sendInvite();
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
