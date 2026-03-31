package com.br.capoeira.eventos.notification.service;

import com.br.capoeira.eventos.notification.model.Event;
import com.br.capoeira.eventos.notification.model.enums.Actions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final Firestore firestore;
    private final FirebaseMessaging firebaseMessaging;
    private static final String COLLECTION_NAME = "events";
    private static final String TOPIC = "event_updates";
    private static final String KEY_ACTION = "action";
    private static final String EVENT_TRANSACTION_ID = "eventTransactionId";

    public void addEvent(Event event) {
        try {
            String transactionId = event.getTransactionId();
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(transactionId).set(event, SetOptions.merge());
            var firebaseEvent = future.get();
            log.info("Event added on firebase: {}", firebaseEvent.toString());
        } catch (Exception e) {
            log.error("Event not added to Firebase {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addMultipleEventsBatch(List<Event> events) {
        try {
            WriteBatch batch = firestore.batch();
            List<String> usedIds = new ArrayList<>();

            for (Event event : events) {
                String transactionId = event.getTransactionId();
                DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(transactionId);
                batch.set(docRef, event, SetOptions.merge());
                usedIds.add(transactionId);

                log.info("Event transaction ID '{}' add to the set with merge.", transactionId);
            }

            ApiFuture<List<WriteResult>> future = batch.commit();
            List<WriteResult> results = future.get();

            log.info("Set of {} events commited. Total of operations: {}", results.size(), usedIds.size());

        } catch (Exception e) {
            log.error("Events Get ALL not added to Firebase {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateEvent(Event event) {
        try {
            var transactionId = event.getTransactionId();
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                    .document(transactionId)
                    .set(event, SetOptions.merge());
            WriteResult result = future.get();
            log.info("Event {} updated on Firestore (with merge) in: {}", transactionId, result.getUpdateTime());
        } catch (Exception e) {
            log.error("Event not updated to Firebase {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendEventUpdateNotification(String transactionId, Actions action) {
        try {
            Message message = getMessage(transactionId, action);

            String response = firebaseMessaging.send(message);
            log.info("Message send on topic {} with success : {} ", TOPIC, response);
        } catch (FirebaseMessagingException e) {
            log.error("Message send on topic {} could not be sent : {} ", TOPIC, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Message getMessage(String transactionId, Actions action) {
        var builder = Message.builder();
        builder.putData(KEY_ACTION, action.name());
        if (!isEmpty(transactionId)) {
            builder.putData(EVENT_TRANSACTION_ID, transactionId);
        }
        return builder
                .setTopic(TOPIC)
                .build();
    }

}