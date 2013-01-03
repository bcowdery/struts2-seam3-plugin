/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ValidationAware;
import org.jboss.seam.international.status.Level;
import org.jboss.seam.international.status.Message;
import org.jboss.seam.international.status.MessageBuilder;
import org.jboss.seam.international.status.MessageFactory;
import org.jboss.seam.international.status.Messages;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides a default implementation for the most common actions, making use of the Seam 3 international CDI module
 * to provide message handling, locale support and text provider functions instead of the default Struts2 facilities.

 * @see SeamTextProvider
 * @see SeamTextProviderSupport
 *
 * @author Brian Cowdery
 * @since  28-Sept-2012
 */
public class SeamActionSupport extends SeamTextProviderSupport implements Action, ValidationAware, Serializable {

    private Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
    private Collection<String> actionErrors = new ArrayList<>();
    private Collection<String> actionMessages = new ArrayList<>();

    @Inject MessageFactory factory;
    @Inject Messages messages;

    public SeamActionSupport() {
    }

    public Messages getMessages() {
        return messages;
    }

    public Set<Message> getAllMessages() {
        return messages.getAll();
    }

    /* Action */

    /**
     * A default implementation that does nothing an returns "success".
     *
     * Subclasses should override this method to provide their business logic.
     *
     * See also {@link com.opensymphony.xwork2.Action#execute()}.
     *
     * @return returns {@link #SUCCESS}
     * @throws Exception can be thrown by subclasses.
     */
    public String execute() throws Exception {
        return SUCCESS;
    }

    /**
     * A default action for gathering user input. This method is ignored by validation when using
     * the "struts-default" and "seam-default" stacks, and will not trigger validation errors.
     *
     * Use an input action to signal that user input is required, perform the necessary
     * setup and populate the input form.
     *
     * @return returns {@link #INPUT}
     * @throws Exception can be thrown by subclasses.
     */
    public String input() throws Exception {
        return INPUT;
    }

    /* ValidationAware */

    public void setActionErrors(Collection<String> errorMessages) {
        for (String message : errorMessages) {
            MessageBuilder builder = factory.error(message);
            this.messages.add(builder.build());
        }
    }

    public Collection<String> getActionErrors() {
        buildMessages();
        return actionErrors;
    }

    public void setActionMessages(Collection<String> messages) {
        for (String message : messages) {
            MessageBuilder builder = factory.info(message);
            this.messages.add(builder.build());
        }
    }

    public Collection<String> getActionMessages() {
        buildMessages();
        return actionMessages;
    }

    /**
     * Processes the stored messages into the final rendered text and adds them
     * to either the action error, or action message list depending on the severity.
     *
     * Once processed and added to the list for rendering, the messages will be cleared
     * from the queue and will not be shown again.
     */
    private void buildMessages() {
        if (messages.isEmpty())
            return;

        for (Message message : messages.getAll()) {
            if (message.getLevel() == Level.FATAL || message.getLevel() == Level.ERROR) {
                actionErrors.add(message.getText());
            } else {
                actionMessages.add(message.getText());
            }
        }
        messages.clear();
    }

    public void setFieldErrors(Map<String, List<String>> errorMap) {
        this.fieldErrors = errorMap;
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }

    public void addActionError(String anErrorMessage) {
        messages.error(anErrorMessage);
    }

    public void addActionMessage(String aMessage) {
        messages.info(aMessage);
    }

    public void addFieldError(String fieldName, String errorMessage) {
        if (!fieldErrors.containsKey(fieldName)) {
            fieldErrors.put(fieldName, new ArrayList<String>());
        }

        fieldErrors.get(fieldName).add(errorMessage);
    }

    public boolean hasActionMessages() {
        return !getActionMessages().isEmpty();
    }

    public boolean hasActionErrors() {
        return !getActionErrors().isEmpty();
    }

    public boolean hasErrors() {
        return hasFieldErrors() || hasActionErrors();
    }

    public boolean hasFieldErrors() {
        return !getFieldErrors().isEmpty();
    }
}
