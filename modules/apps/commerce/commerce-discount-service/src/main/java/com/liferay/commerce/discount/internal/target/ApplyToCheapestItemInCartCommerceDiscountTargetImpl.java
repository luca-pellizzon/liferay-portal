/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.discount.internal.target;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountRel;
import com.liferay.commerce.discount.model.CommerceDiscountRelTable;
import com.liferay.commerce.discount.model.CommerceDiscountTable;
import com.liferay.commerce.discount.service.CommerceDiscountRelLocalService;
import com.liferay.commerce.discount.target.CommerceDiscountSKUTarget;
import com.liferay.commerce.discount.target.CommerceDiscountTarget;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.util.CommerceBigDecimalUtil;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.ExistsFilter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.math.BigDecimal;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.discount.target.key=" + CommerceDiscountConstants.TARGET_CHEAPEST_ITEM_IN_CART,
		"commerce.discount.target.order:Integer=20"
	},
	service = {CommerceDiscountSKUTarget.class, CommerceDiscountTarget.class}
)
public class ApplyToCheapestItemInCartCommerceDiscountTargetImpl
	implements CommerceDiscountSKUTarget, CommerceDiscountTarget {

	@Override
	public void contributeDocument(
		Document document, CommerceDiscount commerceDiscount) {

		List<CommerceDiscountRel> commerceDiscountRels =
			_commerceDiscountRelLocalService.getCommerceDiscountRels(
				commerceDiscount.getCommerceDiscountId(),
				CPInstance.class.getName());

		Stream<CommerceDiscountRel> stream = commerceDiscountRels.stream();

		LongStream longStream = stream.mapToLong(
			CommerceDiscountRel::getClassPK);

		long[] cpInstanceIds = longStream.toArray();

		document.addKeyword(
			"commerce_discount_target_cp_instance_ids", cpInstanceIds);
	}

	@Override
	public String getKey() {
		return CommerceDiscountConstants.TARGET_CHEAPEST_ITEM_IN_CART;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return _language.get(
			resourceBundle,
			CommerceDiscountConstants.TARGET_CHEAPEST_ITEM_IN_CART);
	}

	@Override
	public Type getType() {
		return Type.APPLY_TO_SKU;
	}

	@Override
	public boolean isApplicable(
			CommerceContext commerceContext, CommerceDiscount commerceDiscount,
			CPInstance cpInstance)
		throws PortalException {

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder == null) {
			return false;
		}

		BigDecimal lowestPrice = BigDecimal.ONE.negate();
		long lowestPriceCPInstance = 0;

		for (CommerceOrderItem commerceOrderItem :
				commerceOrder.getCommerceOrderItems()) {

			BigDecimal unitPrice = commerceOrderItem.getUnitPrice();
			BigDecimal promoPrice = commerceOrderItem.getPromoPrice();

			BigDecimal activePrice = unitPrice;

			if (CommerceBigDecimalUtil.gt(unitPrice, promoPrice) &&
				!CommerceBigDecimalUtil.isZero(promoPrice)) {

				activePrice = promoPrice;
			}

			if (CommerceBigDecimalUtil.eq(
					lowestPrice, BigDecimal.ONE.negate()) ||
				CommerceBigDecimalUtil.gte(lowestPrice, activePrice)) {

				lowestPrice = activePrice;
				lowestPriceCPInstance = commerceOrderItem.getCPInstanceId();
			}
		}

		if (lowestPriceCPInstance == cpInstance.getCPInstanceId()) {
			return true;
		}

		return false;
	}

	@Override
	public void postProcessContextBooleanFilter(
		BooleanFilter contextBooleanFilter, CPInstance cpInstance) {

		BooleanFilter booleanFilter = new BooleanFilter();

		booleanFilter.add(
			new BooleanFilter() {
				{
					add(
						new ExistsFilter(
							"commerce_discount_target_cp_instance_ids"),
						BooleanClauseOccur.MUST_NOT);
				}
			},
			BooleanClauseOccur.SHOULD);
		booleanFilter.add(
			new TermFilter(
				"commerce_discount_target_cp_instance_ids",
				String.valueOf(cpInstance.getCPDefinitionId())),
			BooleanClauseOccur.SHOULD);

		contextBooleanFilter.add(booleanFilter, BooleanClauseOccur.MUST);
	}

	public Predicate targetPredicateContributor(
		long cpDefinitionId, long cpInstanceId) {

		return CommerceDiscountTable.INSTANCE.target.eq(
			CommerceDiscountConstants.TARGET_CHEAPEST_ITEM_IN_CART
		).and(
			Predicate.withParentheses(
				CommerceDiscountRelTable.INSTANCE.classPK.eq(
					cpInstanceId
				).and(
					CommerceDiscountRelTable.INSTANCE.classNameId.eq(
						_classNameLocalService.getClassNameId(
							CPInstance.class.getName()))
				).or(
					CommerceDiscountRelTable.INSTANCE.commerceDiscountRelId.
						isNull()
				))
		);
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CommerceDiscountRelLocalService _commerceDiscountRelLocalService;

	@Reference
	private Language _language;

}