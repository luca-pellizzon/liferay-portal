/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.client.dto.v1_0;

import com.liferay.headless.object.client.function.UnsafeSupplier;
import com.liferay.headless.object.client.serdes.v1_0.ObjectEntryCMSBulkActionRequestSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Alicia García
 * @generated
 */
@Generated("")
public class ObjectEntryCMSBulkActionRequest
	implements Cloneable, Serializable {

	public static ObjectEntryCMSBulkActionRequest toDTO(String json) {
		return ObjectEntryCMSBulkActionRequestSerDes.toDTO(json);
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public void setIds(UnsafeSupplier<Long[], Exception> idsUnsafeSupplier) {
		try {
			ids = idsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long[] ids;

	public com.liferay.headless.object.client.permission.Permission[]
		getPermissions() {

		return permissions;
	}

	public void setPermissions(
		com.liferay.headless.object.client.permission.Permission[]
			permissions) {

		this.permissions = permissions;
	}

	public void setPermissions(
		UnsafeSupplier
			<com.liferay.headless.object.client.permission.Permission[],
			 Exception> permissionsUnsafeSupplier) {

		try {
			permissions = permissionsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected com.liferay.headless.object.client.permission.Permission[]
		permissions;

	@Override
	public ObjectEntryCMSBulkActionRequest clone()
		throws CloneNotSupportedException {

		return (ObjectEntryCMSBulkActionRequest)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ObjectEntryCMSBulkActionRequest)) {
			return false;
		}

		ObjectEntryCMSBulkActionRequest objectEntryCMSBulkActionRequest =
			(ObjectEntryCMSBulkActionRequest)object;

		return Objects.equals(
			toString(), objectEntryCMSBulkActionRequest.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ObjectEntryCMSBulkActionRequestSerDes.toJSON(this);
	}

}