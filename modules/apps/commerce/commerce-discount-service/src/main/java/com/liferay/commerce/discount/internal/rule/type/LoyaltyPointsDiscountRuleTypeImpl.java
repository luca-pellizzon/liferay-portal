package com.liferay.commerce.discount.internal.rule.type;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.discount.constants.CommerceDiscountRuleConstants;
import com.liferay.commerce.discount.model.CommerceDiscountRule;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleType;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.object.service.ObjectEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import org.osgi.service.component.annotations.Component;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@Component(
	immediate = true,
	property = {
		"commerce.discount.rule.type.key=loyalty-point",
		"commerce.discount.rule.type.order:Integer=50"
	},
	service = CommerceDiscountRuleType.class
)
public class LoyaltyPointsDiscountRuleTypeImpl implements CommerceDiscountRuleType{

	@Override
	public boolean evaluate(
		CommerceDiscountRule commerceDiscountRule,
		CommerceContext commerceContext) throws PortalException {

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		ObjectDefinition objectDefinition =
			ObjectDefinitionLocalServiceUtil.fetchSystemObjectDefinition("AccountEntry");

		if(objectDefinition == null){
			return false;
		}

		ObjectEntry objectEntry = ObjectEntryLocalServiceUtil.fetchObjectEntry(
			commerceAccount.getExternalReferenceCode(), objectDefinition.getObjectDefinitionId());

		if(objectEntry == null){
			return false;
		}

		Map<String, Serializable> values = objectEntry.getValues();

		Long loyaltyPoints = (Long) values.get("points");

		if(loyaltyPoints > 9){
			return true;
		}

		return false;
	}

	@Override
	public String getKey() {
		return "loyalty-point";
	}

	@Override
	public String getLabel(Locale locale) {
		return "Loyalty Point";
	}
}
