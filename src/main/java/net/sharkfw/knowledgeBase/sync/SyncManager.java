package net.sharkfw.knowledgeBase.sync;

import net.sharkfw.knowledgeBase.*;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.SharkEngine;
import org.apache.maven.wagon.providers.ssh.jsch.ScpCommandExecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 14.09.16.
 */
public class SyncManager {

    public interface SyncInviteListener {
        void onInvitation(SyncComponent component);
    }

    public static final String SHARK_SYNC_INVITE_TYPE_SI = "http://www.sharksystem.net/sync/invite";
//    public static final String SHARK_SYNC_ACCEPT_TYPE_SI = "http://www.sharksystem.net/sync/accept";
    public static final String SHARK_SYNC_OFFER_TYPE_SI = "http://www.sharksystem.net/sync/offer";
    public static final String SHARK_SYNC_MERGE_TYPE_SI = "http://www.sharksystem.net/sync/merge";

    public static final SemanticTag SHARK_SYNC_INVITE_TAG = InMemoSharkKB.createInMemoSemanticTag("SYNC_INVITE", SHARK_SYNC_INVITE_TYPE_SI);
//    public static final SemanticTag SHARK_SYNC_ACCEPT_TAG = InMemoSharkKB.createInMemoSemanticTag("SYNC_ACCEPT", SHARK_SYNC_ACCEPT_TYPE_SI);
    public static final SemanticTag SHARK_SYNC_OFFER_TAG = InMemoSharkKB.createInMemoSemanticTag("SYNC_OFFER", SHARK_SYNC_OFFER_TYPE_SI);
    public static final SemanticTag SHARK_SYNC_MERGE_TAG = InMemoSharkKB.createInMemoSemanticTag("SYNC_MERGE", SHARK_SYNC_MERGE_TYPE_SI);

    private SharkEngine engine;

    private static SyncManager sInstance = null;
    private List<SyncComponent> components = new ArrayList<>();
    private List<SyncInviteListener> listeners = new ArrayList<>();

    public static SyncManager getInstance(SharkEngine engine) {
        if(sInstance == null){
            sInstance = new SyncManager(engine);
        }
        return sInstance;
    }

    private SyncManager(SharkEngine engine) {
        this.engine = engine;
    }

    public void allowInvitation(boolean allow){
        // TODO activate or deactivate InvitationKP
    }

    public SyncComponent createSyncComponent(
            SharkKB kb,
            SemanticTag uniqueName,
            PeerSTSet member,
            PeerSemanticTag owner,
            boolean writable){

        if(getComponentByName(uniqueName)!= null) return null;

        SyncComponent component = null;
        try {
            component = new SyncComponent(engine, kb, uniqueName, member, owner, writable);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        components.add(component);
        return component;
    }

    public void removeSyncComponent(SyncComponent component){
        components.remove(component);
    }

    public Iterator<SyncComponent> getSyncComponents(){
        return components.iterator();
    }

    public SyncComponent getComponentByName(SemanticTag name){
        for (SyncComponent component : components ){
            if(SharkCSAlgebra.identical(component.getUniqueName(), name) ){
                return component;
            }
        }
        return null;
    }

    public void addInviteListener(SyncInviteListener listener){
        listeners.add(listener);
    }

    public void removeInviteListener(SyncInviteListener listener){
        listeners.remove(listener);
    }

}