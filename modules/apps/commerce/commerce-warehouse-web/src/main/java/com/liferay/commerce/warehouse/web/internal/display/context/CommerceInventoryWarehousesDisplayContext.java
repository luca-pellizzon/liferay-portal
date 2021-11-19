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

package com.liferay.commerce.warehouse.web.internal.display.context;

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseService;
import com.liferay.commerce.price.list.constants.CommercePriceListScreenNavigationConstants;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelRel;
import com.liferay.commerce.product.service.CommerceChannelRelService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.frontend.taglib.clay.data.set.servlet.taglib.util.ClayDataSetActionDropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;
import java.util.stream.Stream;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Di Giorgi
 * @author Alessio Antonio Rendina
 */
public class CommerceInventoryWarehousesDisplayContext {

	public CommerceInventoryWarehousesDisplayContext(
		CommerceChannelRelService commerceChannelRelService,
		CommerceChannelService commerceChannelService,
		CommerceInventoryWarehouseService commerceInventoryWarehouseService,
		HttpServletRequest httpServletRequest) {

		_commerceChannelRelService = commerceChannelRelService;
		_commerceChannelService = commerceChannelService;
		_commerceInventoryWarehouseService = commerceInventoryWarehouseService;
		_httpServletRequest = httpServletRequest;

		_cpRequestHelper = new CPRequestHelper(httpServletRequest);
	}

	public long[] getCommerceChannelRelCommerceChannelIds()
		throws PortalException {

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			getCommerceInventoryWarehouse();

		if (commerceInventoryWarehouse == null) {
			return new long[0];
		}

		List<CommerceChannelRel> commerceChannelRels =
			_commerceChannelRelService.getCommerceChannelRels(
				CommerceInventoryWarehouse.class.getName(),
				commerceInventoryWarehouse.getCommerceInventoryWarehouseId(),
				null, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Stream<CommerceChannelRel> stream = commerceChannelRels.stream();

		return stream.mapToLong(
			CommerceChannelRel::getCommerceChannelId
		).toArray();
	}

	public List<CommerceChannel> getCommerceChannels() throws PortalException {
		return _commerceChannelService.getCommerceChannels(
			_cpRequestHelper.getCompanyId());
	}

	public CommerceInventoryWarehouse getCommerceInventoryWarehouse()
		throws PortalException {

		if (_commerceInventoryWarehouse != null) {
			return _commerceInventoryWarehouse;
		}

		long commerceInventoryWarehouseId = ParamUtil.getLong(
			_cpRequestHelper.getRenderRequest(),
			"commerceInventoryWarehouseId");

		if (commerceInventoryWarehouseId > 0) {
			_commerceInventoryWarehouse =
				_commerceInventoryWarehouseService.
					getCommerceInventoryWarehouse(commerceInventoryWarehouseId);
		}

		return _commerceInventoryWarehouse;
	}

	public CreationMenu getCreationMenu() {
		return CreationMenuBuilder.addDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(
					PortletURLBuilder.createRenderURL(
						_cpRequestHelper.getLiferayPortletResponse()
					).setMVCRenderCommandName(
						"/commerce_inventory_warehouse" +
							"/edit_commerce_inventory_warehouse"
					).setBackURL(
						_cpRequestHelper.getCurrentURL()
					).setWindowState(
						LiferayWindowState.POP_UP
					).buildString());
				dropdownItem.setLabel("add-warehouse");
				dropdownItem.setTarget("modal");
			}
		).build();
	}

	public List<ClayDataSetActionDropdownItem>
	getWarehouseClayDataSetActionDropdownItems()
		throws PortalException {

		List<ClayDataSetActionDropdownItem> clayDataSetActionDropdownItems =
			getClayDataSetActionDropdownItems(
				PortletURLBuilder.createRenderURL(
					commercePricingRequestHelper.getRenderResponse()
				).setMVCRenderCommandName(
					"/commerce_price_list/edit_commerce_price_list"
				).setRedirect(
					commercePricingRequestHelper.getCurrentURL()
				).setParameter(
					"commercePriceListId", "{id}"
				).setParameter(
					"screenNavigationCategoryKey",
					CommercePriceListScreenNavigationConstants.
						CATEGORY_KEY_DETAILS
				).buildString(),
				false);

		clayDataSetActionDropdownItems.add(
			new ClayDataSetActionDropdownItem(
				_getManagePriceListPermissionsURL(), null, "permissions",
				LanguageUtil.get(httpServletRequest, "permissions"), "get",
				"permissions", "modal-permissions"));

		return clayDataSetActionDropdownItems;
	}

	public PortletURL getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			_cpRequestHelper.getRenderResponse()
		).buildPortletURL();
	}

	public boolean hasManageCommerceInventoryWarehousePermission() {
		return true;
	}

	private final CommerceChannelRelService _commerceChannelRelService;
	private final CommerceChannelService _commerceChannelService;
	private CommerceInventoryWarehouse _commerceInventoryWarehouse;
	private final CommerceInventoryWarehouseService
		_commerceInventoryWarehouseService;
	private final CPRequestHelper _cpRequestHelper;
	private final HttpServletRequest _httpServletRequest;

}