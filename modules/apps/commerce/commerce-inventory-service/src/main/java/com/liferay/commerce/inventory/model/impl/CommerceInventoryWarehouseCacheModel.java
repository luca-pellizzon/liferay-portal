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

package com.liferay.commerce.inventory.model.impl;

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing CommerceInventoryWarehouse in entity cache.
 *
 * @author Luca Pellizzon
 * @generated
 */
public class CommerceInventoryWarehouseCacheModel
	implements CacheModel<CommerceInventoryWarehouse>, Externalizable,
			   MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CommerceInventoryWarehouseCacheModel)) {
			return false;
		}

		CommerceInventoryWarehouseCacheModel
			commerceInventoryWarehouseCacheModel =
				(CommerceInventoryWarehouseCacheModel)object;

		if ((commerceInventoryWarehouseId ==
				commerceInventoryWarehouseCacheModel.
					commerceInventoryWarehouseId) &&
			(mvccVersion == commerceInventoryWarehouseCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, commerceInventoryWarehouseId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(43);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", externalReferenceCode=");
		sb.append(externalReferenceCode);
		sb.append(", commerceInventoryWarehouseId=");
		sb.append(commerceInventoryWarehouseId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", active=");
		sb.append(active);
		sb.append(", city=");
		sb.append(city);
		sb.append(", commerceRegionCode=");
		sb.append(commerceRegionCode);
		sb.append(", countryTwoLettersISOCode=");
		sb.append(countryTwoLettersISOCode);
		sb.append(", description=");
		sb.append(description);
		sb.append(", latitude=");
		sb.append(latitude);
		sb.append(", longitude=");
		sb.append(longitude);
		sb.append(", name=");
		sb.append(name);
		sb.append(", type=");
		sb.append(type);
		sb.append(", street1=");
		sb.append(street1);
		sb.append(", street2=");
		sb.append(street2);
		sb.append(", street3=");
		sb.append(street3);
		sb.append(", zip=");
		sb.append(zip);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public CommerceInventoryWarehouse toEntityModel() {
		CommerceInventoryWarehouseImpl commerceInventoryWarehouseImpl =
			new CommerceInventoryWarehouseImpl();

		commerceInventoryWarehouseImpl.setMvccVersion(mvccVersion);

		if (externalReferenceCode == null) {
			commerceInventoryWarehouseImpl.setExternalReferenceCode("");
		}
		else {
			commerceInventoryWarehouseImpl.setExternalReferenceCode(
				externalReferenceCode);
		}

		commerceInventoryWarehouseImpl.setCommerceInventoryWarehouseId(
			commerceInventoryWarehouseId);
		commerceInventoryWarehouseImpl.setCompanyId(companyId);
		commerceInventoryWarehouseImpl.setUserId(userId);

		if (userName == null) {
			commerceInventoryWarehouseImpl.setUserName("");
		}
		else {
			commerceInventoryWarehouseImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			commerceInventoryWarehouseImpl.setCreateDate(null);
		}
		else {
			commerceInventoryWarehouseImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			commerceInventoryWarehouseImpl.setModifiedDate(null);
		}
		else {
			commerceInventoryWarehouseImpl.setModifiedDate(
				new Date(modifiedDate));
		}

		commerceInventoryWarehouseImpl.setActive(active);

		if (city == null) {
			commerceInventoryWarehouseImpl.setCity("");
		}
		else {
			commerceInventoryWarehouseImpl.setCity(city);
		}

		if (commerceRegionCode == null) {
			commerceInventoryWarehouseImpl.setCommerceRegionCode("");
		}
		else {
			commerceInventoryWarehouseImpl.setCommerceRegionCode(
				commerceRegionCode);
		}

		if (countryTwoLettersISOCode == null) {
			commerceInventoryWarehouseImpl.setCountryTwoLettersISOCode("");
		}
		else {
			commerceInventoryWarehouseImpl.setCountryTwoLettersISOCode(
				countryTwoLettersISOCode);
		}

		if (description == null) {
			commerceInventoryWarehouseImpl.setDescription("");
		}
		else {
			commerceInventoryWarehouseImpl.setDescription(description);
		}

		commerceInventoryWarehouseImpl.setLatitude(latitude);
		commerceInventoryWarehouseImpl.setLongitude(longitude);

		if (name == null) {
			commerceInventoryWarehouseImpl.setName("");
		}
		else {
			commerceInventoryWarehouseImpl.setName(name);
		}

		if (type == null) {
			commerceInventoryWarehouseImpl.setType("");
		}
		else {
			commerceInventoryWarehouseImpl.setType(type);
		}

		if (street1 == null) {
			commerceInventoryWarehouseImpl.setStreet1("");
		}
		else {
			commerceInventoryWarehouseImpl.setStreet1(street1);
		}

		if (street2 == null) {
			commerceInventoryWarehouseImpl.setStreet2("");
		}
		else {
			commerceInventoryWarehouseImpl.setStreet2(street2);
		}

		if (street3 == null) {
			commerceInventoryWarehouseImpl.setStreet3("");
		}
		else {
			commerceInventoryWarehouseImpl.setStreet3(street3);
		}

		if (zip == null) {
			commerceInventoryWarehouseImpl.setZip("");
		}
		else {
			commerceInventoryWarehouseImpl.setZip(zip);
		}

		commerceInventoryWarehouseImpl.resetOriginalValues();

		return commerceInventoryWarehouseImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		externalReferenceCode = objectInput.readUTF();

		commerceInventoryWarehouseId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		active = objectInput.readBoolean();
		city = objectInput.readUTF();
		commerceRegionCode = objectInput.readUTF();
		countryTwoLettersISOCode = objectInput.readUTF();
		description = objectInput.readUTF();

		latitude = objectInput.readDouble();

		longitude = objectInput.readDouble();
		name = objectInput.readUTF();
		type = objectInput.readUTF();
		street1 = objectInput.readUTF();
		street2 = objectInput.readUTF();
		street3 = objectInput.readUTF();
		zip = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (externalReferenceCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(externalReferenceCode);
		}

		objectOutput.writeLong(commerceInventoryWarehouseId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeBoolean(active);

		if (city == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(city);
		}

		if (commerceRegionCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(commerceRegionCode);
		}

		if (countryTwoLettersISOCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(countryTwoLettersISOCode);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeDouble(latitude);

		objectOutput.writeDouble(longitude);

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (type == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(type);
		}

		if (street1 == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(street1);
		}

		if (street2 == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(street2);
		}

		if (street3 == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(street3);
		}

		if (zip == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(zip);
		}
	}

	public long mvccVersion;
	public String externalReferenceCode;
	public long commerceInventoryWarehouseId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public boolean active;
	public String city;
	public String commerceRegionCode;
	public String countryTwoLettersISOCode;
	public String description;
	public double latitude;
	public double longitude;
	public String name;
	public String type;
	public String street1;
	public String street2;
	public String street3;
	public String zip;

}