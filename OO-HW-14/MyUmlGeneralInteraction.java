import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private Class classes = new Class();
    private TimeSequence timeSequence = new TimeSequence();
    private StateMachine stateMachine = new StateMachine();

    public MyUmlGeneralInteraction(UmlElement... elements) {
        for (UmlElement e : elements) {
            switch (e.getElementType()) {
                case UML_CLASS:
                    classes.addClass(e);
                    break;
                case UML_ATTRIBUTE:
                    UmlAttribute attr = (UmlAttribute) e;
                    if (timeSequence.judgeWhetherTs((attr))) {
                        timeSequence.add(attr);
                    } else {
                        classes.addAttribute(attr);
                    }
                    break;
                case UML_OPERATION:
                    classes.addOperation((UmlOperation) e);
                    break;
                case UML_PARAMETER:
                    classes.addParameter((UmlParameter) e);
                    break;
                case UML_ASSOCIATION:
                    classes.addAssociation((UmlAssociation) e);
                    break;
                case UML_ASSOCIATION_END:
                    classes.addAssociationEnd((UmlAssociationEnd) e);
                    break;
                case UML_INTERFACE:
                    classes.addClass(e);
                    break;
                case UML_INTERFACE_REALIZATION:
                    classes.addInterfaceRealization(
                            (UmlInterfaceRealization) e);
                    break;
                case UML_GENERALIZATION:
                    classes.addGeneralization((UmlGeneralization) e);
                    break;
                case UML_INTERACTION:
                    timeSequence.add(e);
                    break;
                case UML_LIFELINE:
                    timeSequence.add(e);
                    break;
                case UML_MESSAGE:
                    timeSequence.add(e);
                    break;
                case UML_ENDPOINT:
                    timeSequence.add(e);
                    break;
                case UML_REGION:
                    stateMachine.add(e);
                    break;
                case UML_STATE_MACHINE:
                    stateMachine.add(e);
                    break;
                case UML_PSEUDOSTATE:
                    stateMachine.add(e);
                    break;
                case UML_STATE:
                    stateMachine.add(e);
                    break;
                case UML_FINAL_STATE:
                    stateMachine.add(e);
                    break;
                case UML_EVENT:
                    stateMachine.add(e);
                    break;
                case UML_OPAQUE_BEHAVIOR:
                    stateMachine.add(e);
                    break;
                case UML_TRANSITION:
                    stateMachine.add(e);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getClassCount() {
        return classes.countClasses();
    }

    @Override
    public int getClassOperationCount(
            String s, OperationQueryType operationQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getClassOperationCount(s, operationQueryType);
        }
    }

    @Override
    public int getClassAttributeCount(
            String s, AttributeQueryType attributeQueryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getClassAttributeCount(s, attributeQueryType);
        }
    }

    @Override
    public int getClassAssociationCount(String s)
            throws ClassNotFoundException,
            ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getClassAssociationCount(s);
        }
    }

    @Override
    public List<String> getClassAssociatedClassList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getAssociation(s);
        }
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getClassOperationVisibility(s, s1);
        }
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else if (!classes.foundAttr(s, s1)) {
            throw new AttributeNotFoundException(s, s1);
        } else if (classes.isDuplicatedAttr(s, s1)) {
            throw new AttributeDuplicatedException(s, s1);
        } else {
            return classes.getClassAttributeVisibility(s, s1);
        }
    }

    @Override
    public String getTopParentClass(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getTopClass(s);
        }
    }

    @Override
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        } else {
            return classes.getImplementInterfaceList(s);
        }
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(
            String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.foundClass(s)) {
            throw new ClassNotFoundException(s);
        } else if (classes.isDuplicated(s)) {
            throw new ClassDuplicatedException(s);
        }
        return classes.getInformationNotHidden(s);
    }

    @Override
    public int getParticipantCount(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        int result = timeSequence.getParticipantCount(s);
        if (result == -1) {
            throw new InteractionNotFoundException(s);
        } else if (result == -2) {
            throw new InteractionDuplicatedException(s);
        }
        return result;
    }

    @Override
    public int getMessageCount(String s)
            throws InteractionNotFoundException,
            InteractionDuplicatedException {
        int result = timeSequence.getMessageCount(s);
        if (result == -1) {
            throw new InteractionNotFoundException(s);
        } else if (result == -2) {
            throw new InteractionDuplicatedException(s);
        }
        return result;
    }

    @Override
    public int getIncomingMessageCount(String s, String s1)
            throws InteractionNotFoundException,
            InteractionDuplicatedException,
            LifelineNotFoundException,
            LifelineDuplicatedException {
        int result = timeSequence.getIncomingMessageCount(s, s1);
        if (result == -1) {
            throw new InteractionNotFoundException(s);
        } else if (result == -2) {
            throw new InteractionDuplicatedException(s);
        } else if (result == -3) {
            throw new LifelineNotFoundException(s, s1);
        } else if (result == -4) {
            throw new LifelineDuplicatedException(s, s1);
        }
        return result;
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        Set<AttributeClassInformation> result = classes.getRepeatedInfo();
        if (result.size() != 0) {
            throw new UmlRule002Exception(result);
        }
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        Set<UmlClassOrInterface> uml008 = classes.getCircleInfos();
        if (uml008.size() != 0) {
            throw new UmlRule008Exception(uml008);
        }
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        HashSet<UmlClassOrInterface> uml009 = classes.getGeneInfos();
        if (uml009.size() != 0) {
            throw new UmlRule009Exception(uml009);
        }
    }

    @Override
    public int getStateCount(String s) throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        int result = stateMachine.getStateCount(s);
        if (result == -1) {
            throw new StateMachineNotFoundException(s);
        } else if (result == -2) {
            throw new StateMachineDuplicatedException(s);
        }
        return result;
    }

    @Override
    public int getTransitionCount(String s)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        int result = stateMachine.getTransitionCount(s);
        if (result == -1) {
            throw new StateMachineNotFoundException(s);
        } else if (result == -2) {
            throw new StateMachineDuplicatedException(s);
        }
        return result;
    }

    @Override
    public int getSubsequentStateCount(String s, String s1)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        int result = stateMachine.getSubsequentStateCount(s, s1);
        if (result == -1) {
            throw new StateMachineNotFoundException(s);
        } else if (result == -2) {
            throw new StateMachineDuplicatedException(s);
        } else if (result == -3) {
            throw new StateNotFoundException(s, s1);
        } else if (result == -4) {
            throw new StateDuplicatedException(s, s1);
        }
        return result;
    }
}