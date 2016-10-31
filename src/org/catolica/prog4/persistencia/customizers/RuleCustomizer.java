package org.catolica.prog4.persistencia.customizers;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.OneToOneMapping;

/**
 *
 * @author FCGF
 */
public class RuleCustomizer implements DescriptorCustomizer {

    @Override
    public void customize(ClassDescriptor cd) throws Exception {
        OneToOneMapping mapping = (OneToOneMapping) cd.getMappingForAttributeName("rule");
        ExpressionBuilder builder = new ExpressionBuilder();
        mapping.setSelectionCriteria(builder.getField("User.rule_id").equal(builder.getParameter("Rule.rule_id")));
    }

}
