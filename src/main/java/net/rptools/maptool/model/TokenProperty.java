/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.model;

import com.google.protobuf.StringValue;

import java.io.Serializable;
import java.util.Objects;

import net.rptools.maptool.server.proto.TokenPropertyDto;

public class TokenProperty implements DisplayNames, Serializable {
    private String name;
    private String shortName;
    private String displayName;
    private boolean playerEditable = true;
    private PermissionsScope visibilityPermission = PermissionsScope.NONE;
    private VariableType variableType = VariableType.UNDEFINED;
    private String defaultValue = "";

    public TokenProperty() {
        // For serialization
    }

    public TokenProperty(String name) {
        this(name, null, (String) null);
    }

    public TokenProperty(String name, String shortName) {
        this(name, shortName, (String) null);
    }
    public TokenProperty(String name, String shortName, String displayName) {
        this.name = name;
        this.shortName = shortName;
        this.displayName = displayName;
    }
    public TokenProperty(String name, VariableType variableType) {
        this(name, null, null, true, variableType, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, boolean playerEditable) {
        this(name, null, null, playerEditable, null, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, PermissionsScope visibilityPermission) {
        this(name, null, null, true, null, visibilityPermission, null);
    }

    public TokenProperty(String name, boolean playerEditable, VariableType variableType) {
        this(name, null, null, playerEditable, variableType, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, String shortName, VariableType variableType) {
        this(name, shortName, null, true, variableType, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, String shortName, boolean playerEditable) {
        this(name, shortName, null, playerEditable, null, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, String shortName, boolean playerEditable, VariableType variableType) {
        this(name, shortName, null, playerEditable, variableType, PermissionsScope.NONE, null);
    }


    public TokenProperty(String name, String shortName, String displayName, VariableType variableType) {
        this(name, shortName, displayName, true, variableType, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, String shortName, String displayName, boolean playerEditable) {
        this(name, shortName, displayName, playerEditable, null, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, String shortName, String displayName, boolean playerEditable, VariableType variableType) {
        this(name, shortName, displayName, playerEditable, variableType, PermissionsScope.NONE, null);
    }

    public TokenProperty(String name, VariableType variableType, PermissionsScope visibilityPermission) {
        this(name, null, null, true, variableType, visibilityPermission, null);
    }

    public TokenProperty(String name, boolean playerEditable, PermissionsScope visibilityPermission) {
        this(name, null, null, playerEditable, null, visibilityPermission, null);
    }

    public TokenProperty(String name, boolean playerEditable, VariableType variableType, PermissionsScope visibilityPermission) {
        this(name, null, null, playerEditable, variableType, visibilityPermission, null);
    }

    public TokenProperty(String name, String shortName, PermissionsScope visibilityPermission) {
        this(name, shortName, null, true, null, visibilityPermission, null);
    }

    public TokenProperty(String name, String shortName, VariableType variableType, PermissionsScope visibilityPermission) {
        this(name, shortName, null, true, variableType, visibilityPermission, null);
    }

    public TokenProperty(
            String name,
            String shortName,
            boolean playerEditable,
            PermissionsScope visibilityPermission) {
        this(name, shortName, null, playerEditable, null, visibilityPermission, null);
    }

    public TokenProperty(
            String name,
            String shortName,
            boolean playerEditable,
            VariableType variableType,
            PermissionsScope visibilityPermission) {
        this(name, shortName, null, playerEditable, variableType, visibilityPermission, null);
    }

    public TokenProperty(
            String name, String shortName, PermissionsScope visibilityPermission, String defaultValue) {
        this(name, shortName, null, true, null, visibilityPermission, defaultValue);
    }

    public TokenProperty(
            String name, String shortName, VariableType variableType, PermissionsScope visibilityPermission, String defaultValue) {
        this(name, shortName, null, true, variableType, visibilityPermission, defaultValue);
    }

    public TokenProperty(
            String name,
            String shortName,
            boolean playerEditable,
            PermissionsScope visibilityPermission,
            String defaultValue) {
        this(name, shortName, null, playerEditable, null, visibilityPermission, defaultValue);
    }

    public TokenProperty(
            String name,
            String shortName,
            boolean playerEditable,
            VariableType variableType,
            PermissionsScope visibilityPermission,
            String defaultValue) {
        this(name, shortName, null, playerEditable, variableType, visibilityPermission, defaultValue);
    }

    public TokenProperty(
            String name, String shortName, String displayName, PermissionsScope visibilityPermission) {
        this(name, shortName, displayName, true, null, visibilityPermission, null);
    }

    public TokenProperty(
            String name, String shortName, String displayName, VariableType variableType, PermissionsScope visibilityPermission) {
        this(name, shortName, displayName, true, variableType, visibilityPermission, null);
    }

    public TokenProperty(
            String name,
            String shortName,
            String displayName,
            boolean playerEditable,
            PermissionsScope visibilityPermission) {
        this(name, shortName, displayName, playerEditable, null, visibilityPermission, null);
    }

    public TokenProperty(
            String name,
            String shortName,
            String displayName,
            boolean playerEditable,
            VariableType variableType,
            PermissionsScope visibilityPermission) {
        this(name, shortName, displayName, playerEditable, variableType, visibilityPermission, null);
    }

    public TokenProperty(
            String name,
            String shortName,
            String displayName,
            boolean playerEditable,
            VariableType variableType,
            PermissionsScope visibilityPermission,
            String defaultValue) {
        this.name = name;
        this.shortName = shortName;
        this.displayName = displayName;
        this.playerEditable = playerEditable;
        if (variableType != null) {
            this.variableType = variableType;
        }
        if (visibilityPermission != null) {
            this.visibilityPermission = visibilityPermission;
        }
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a new <code>TokenProperty</code> that's a copy of another.
     *
     * @param prop the property to copy the values from.
     */
    public TokenProperty(TokenProperty prop) {
        this.name = prop.name;
        this.shortName = prop.shortName;
        this.displayName = prop.displayName;
        this.playerEditable = prop.playerEditable;
        this.variableType = prop.variableType;
        this.visibilityPermission = prop.visibilityPermission;
        this.defaultValue = prop.defaultValue;
    }

    public static TokenProperty fromDto(TokenPropertyDto dto) {
        var prop = new TokenProperty();
        prop.name = dto.getName();
        prop.shortName = dto.hasShortName() ? dto.getShortName().getValue() : null;

        prop.playerEditable = !dto.hasPlayerEditable() || dto.getPlayerEditable();
        prop.variableType = !dto.hasVariableType() ? VariableType.UNDEFINED : VariableType.valueOf(dto.getVariableType());

        if (dto.hasPermissions()) {
            // the new permissions
            prop.visibilityPermission = PermissionsScope.valueOf(dto.getPermissions());
        } else if (dto.hasHighPriority() && dto.getHighPriority()) {
            // the old permissions
            if (dto.hasGmOnly() && dto.getGmOnly()) {
                prop.visibilityPermission = PermissionsScope.GM;
            } else if (dto.hasOwnerOnly() && dto.getOwnerOnly()) {
                prop.visibilityPermission = PermissionsScope.OWNER;
            } else {
                prop.visibilityPermission = PermissionsScope.ALLIED_ONLY;
            }
        }

        prop.defaultValue = dto.hasDefaultValue() ? dto.getDefaultValue().getValue() : null;
        prop.displayName = dto.hasDisplayName() ? dto.getDisplayName().getValue() : null;
        return prop;
    }

    public boolean isShowOnStatSheet() {
        return visibilityPermission != null
                && !visibilityPermission.equals(PermissionsScope.NONE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasDisplayName() {
        return displayName != null && !displayName.isBlank();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean hasShortName() {
        return shortName != null && !shortName.isBlank();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isPlayerEditable() {
        return playerEditable;
    }

    public void setPlayerEditable(boolean playerEditable) {
        this.playerEditable = playerEditable;
    }

    public PermissionsScope getVisibilityPermission() {
        if(visibilityPermission == null){
            visibilityPermission = PermissionsScope.NONE;
        }
        return visibilityPermission;
    }

    public void setVisibilityPermission(PermissionsScope visibilityPermission) {
        this.visibilityPermission = visibilityPermission;
    }

    public boolean isGMOnly() {
        return visibilityPermission.equals(PermissionsScope.GM);
    }

    public boolean isOwnerOnly() {
        return visibilityPermission.equals(PermissionsScope.OWNER);
    }

    public boolean isAllyOnly() {
        return visibilityPermission.equals(PermissionsScope.ALLIED_ONLY);
    }

    public boolean hasDefaultValue() {
        return defaultValue != null && !defaultValue.isBlank();
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    public VariableType getVariableType() {
        if(variableType == null){
            variableType = VariableType.UNDEFINED;
        }
        return variableType;
    }

    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }

    public TokenPropertyDto toDto() {
        var dto = TokenPropertyDto.newBuilder();
        dto.setName(name);
        dto.setPlayerEditable(playerEditable);
        dto.setVariableType(Objects.requireNonNullElse(variableType, VariableType.UNDEFINED).name());
        dto.setPermissions(Objects.requireNonNullElse(visibilityPermission, PermissionsScope.NONE).name());
        if (hasShortName()) {
            dto.setShortName(StringValue.of(shortName));
        }
        if (hasDisplayName()) {
            dto.setDisplayName(StringValue.of(displayName));
        }
        if (hasDefaultValue()) {
            dto.setDefaultValue(StringValue.of(defaultValue));
        }
        return dto.build();
    }
}
