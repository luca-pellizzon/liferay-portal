package com.liferay.site.cms.site.initializer.internal.util;

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectEntryFolderService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.zip.ZipWriter;

import java.io.IOException;
import java.io.Serializable;

import java.util.List;
import java.util.Map;

public class ZipUtil {

	public static void zipObjectEntry(
			DLAppLocalService dlAppLocalService, ObjectEntry objectEntry,
			ObjectFieldLocalService objectFieldLocalService, String path,
			PermissionChecker permissionChecker, ZipWriter zipWriter)
		throws IOException, PortalException {

		Map<String, Serializable> values = objectEntry.getValues();

		List<ObjectField> objectFields =
			objectFieldLocalService.getObjectFields(
				objectEntry.getObjectDefinitionId());

		String objectFieldName = StringPool.SLASH;

		for (ObjectField objectField : objectFields) {
			if (objectField.compareBusinessType(
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

				objectFieldName = objectField.getName();
			}
		}

		Serializable serializable = values.get(objectFieldName);

		long fileEntryId = GetterUtil.getLong(serializable);

		if (fileEntryId == 0) {
			return;
		}

		FileEntry fileEntry = dlAppLocalService.getFileEntry(fileEntryId);

		if (fileEntry.containsPermission(
				permissionChecker, ActionKeys.DOWNLOAD)) {

			zipWriter.addEntry(
				path + StringPool.SLASH + fileEntry.getFileName(),
				fileEntry.getContentStream());
		}
	}

	public static void zipObjectEntryFolder(
			long groupId, long objectEntryFolderId,
			DLAppLocalService dlAppLocalService,
			ObjectFieldLocalService objectFieldLocalService,
			ObjectEntryFolderService objectEntryFolderService,
			ObjectEntryLocalService objectEntryLocalService, String path,
			ThemeDisplay themeDisplay, ZipWriter zipWriter)
		throws IOException, PortalException {

		List<ObjectEntry> objectEntryFolderObjectEntries =
			objectEntryLocalService.getObjectEntryFolderObjectEntries(
				groupId, objectEntryFolderId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		for (ObjectEntry objectEntry : objectEntryFolderObjectEntries) {
			zipObjectEntry(
				dlAppLocalService, objectEntry, objectFieldLocalService, path,
				themeDisplay.getPermissionChecker(), zipWriter);
		}

		List<ObjectEntryFolder> objectEntryFolders =
			objectEntryFolderService.getObjectEntryFolders(
				groupId, themeDisplay.getCompanyId(), objectEntryFolderId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (ObjectEntryFolder objectEntryFolder : objectEntryFolders) {
			zipObjectEntryFolder(
				groupId, objectEntryFolder.getObjectEntryFolderId(),
				dlAppLocalService, objectFieldLocalService,
				objectEntryFolderService, objectEntryLocalService,
				StringBundler.concat(
					path, StringPool.SLASH, objectEntryFolder.getName()),
				themeDisplay, zipWriter);
		}
	}

}