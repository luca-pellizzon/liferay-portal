/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.object.client.dto.v1_0;

import com.liferay.headless.object.client.function.UnsafeSupplier;
import com.liferay.headless.object.client.serdes.v1_0.ObjectEntryCMSBulkActionSerDes;

import jakarta.annotation.Generated;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author Alicia García
 * @generated
 */
@Generated("")
public class ObjectEntryCMSBulkAction implements Cloneable, Serializable {

	public static ObjectEntryCMSBulkAction toDTO(String json) {
		return ObjectEntryCMSBulkActionSerDes.toDTO(json);
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

	@Override
	public ObjectEntryCMSBulkAction clone() throws CloneNotSupportedException {
		return (ObjectEntryCMSBulkAction)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ObjectEntryCMSBulkAction)) {
			return false;
		}

		ObjectEntryCMSBulkAction objectEntryCMSBulkAction =
			(ObjectEntryCMSBulkAction)object;

		return Objects.equals(toString(), objectEntryCMSBulkAction.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ObjectEntryCMSBulkActionSerDes.toJSON(this);
	}

}