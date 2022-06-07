<%--
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
--%>

<%@ include file="/init.jsp" %>

<liferay-portlet:renderURL var="editCommerceShipmentURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcRenderCommandName" value="/commerce_open_order_content/edit_commerce_shipment" />
	<portlet:param name="commerceOrderId" value="<%= String.valueOf(commerceOrderContentDisplayContext.getCommerceOrderId()) %>" />
</liferay-portlet:renderURL>

<%
CommerceOrder commerceOrder = commerceOrderContentDisplayContext.getCommerceOrder();
%>

<frontend-data-set:classic-display
	contextParams='<%=
		HashMapBuilder.<String, String>put(
			"commerceOrderId", String.valueOf(commerceOrder.getCommerceOrderId())
		).build()
	%>'
	dataProviderKey="<%= CommerceShipmentFDSNames.SHIPMENTS %>"
	id="<%= CommerceShipmentFDSNames.ORDER_SHIPMENTS %>"
	itemsPerPage="<%= 10 %>"
	showManagementBar="<%= false %>"
	style="stacked"
/>