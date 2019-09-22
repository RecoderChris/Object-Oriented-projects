import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAssociation;

import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {
    private Class classes = new Class();

    public MyUmlInteraction(UmlElement... elements) {
        for (UmlElement e : elements) {
            switch (e.getElementType()) {
                case UML_CLASS:
                    classes.addClass(e);
                    break;
                case UML_ATTRIBUTE:
                    classes.addAttribute((UmlAttribute) e);
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
                default:
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
}