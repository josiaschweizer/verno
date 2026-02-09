package ch.verno.ui.verno.settings.setting.shared;

import ch.verno.common.db.dto.table.TenantSettingDto;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class SharedSettings extends VABaseSetting<TenantSettingDto> {

  public static final String TITLE_KEY = "setting.shared_settings";
  @Nonnull
  private final ITenantSettingService tenantSettingService;

  public SharedSettings(@Nonnull final GlobalInterface globalInterface) {
    super(TITLE_KEY, true);

    this.tenantSettingService = globalInterface.getService(ITenantSettingService.class);
    this.dto = tenantSettingService.getCurrentTenantSettingOrDefault();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var enforceQuantityLimits = settingEntryFactory.createBooleanSetting(
            getTranslation("setting.enforce.quantity.settings"),
            Optional.of(getTranslation("setting.when.enabled.quantity.limits.cannot.be.exceeded")),
            binder,
            TenantSettingDto::isEnforceQuantitySettings,
            TenantSettingDto::setEnforceQuantitySettings
    );
    final var enforceCourseLevel = settingEntryFactory.createBooleanSetting(
            getTranslation("setting.enforce.course.level.on.participant.assignment"),
            Optional.of(getTranslation("setting.when.enabled.participants.can.only.be.assigned.to.courses.that.match.their.course.level")),
            binder,
            TenantSettingDto::isEnforceCourseLevelSettings,
            TenantSettingDto::setEnforceCourseLevelSettings
    );
    final var limitCourses = settingEntryFactory.createBooleanSetting(
            getTranslation("setting.limit.course.assignment.to.planned.or.active.courses"),
            Optional.of(getTranslation("setting.when.enabled.participants.can.only.be.assigned.to.courses.that.are.currently.planned.or.active.archived.completed.or.inactive.courses.are.excluded.from.selection")),
            binder,
            TenantSettingDto::isLimitCourseAssignmentsToActive,
            TenantSettingDto::setLimitCourseAssignmentsToActive
    );

    final var mainParentSetting = settingEntryFactory.createToggleSetting(
            getTranslation("setting.which.parent.is.the.main.parent"),
            getTranslation("participant.parent_one"),
            getTranslation("participant.parent_two"),
            Optional.of(getTranslation("setting.defines.which.parent.is.considered.the.main.contact.for.a.participant")),
            binder,
            TenantSettingDto::isParentOneMainParent,
            TenantSettingDto::setParentOneMainParent
    );

    final var content = new VerticalLayout(enforceQuantityLimits, enforceCourseLevel, limitCourses, mainParentSetting);
    content.setPadding(false);

    return content;
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      tenantSettingService.saveCurrentTenantSetting(dto);
    }
  }

  @Nonnull
  @Override
  protected Class<TenantSettingDto> getBeanType() {
    return TenantSettingDto.class;
  }

  @Nonnull
  @Override
  protected TenantSettingDto createNewBeanInstance() {
    return new TenantSettingDto();
  }
}
