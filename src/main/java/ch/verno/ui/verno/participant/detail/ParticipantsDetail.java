package ch.verno.ui.verno.participant.detail;

import ch.verno.common.participant.ParticipantService;
import ch.verno.ui.base.toolbar.ViewToolbar;
import ch.verno.ui.verno.participant.ParticipantDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@Route("participants/detail")
@PageTitle("Participants Detail View")
public class ParticipantsDetail extends VerticalLayout {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final Binder<ParticipantDto> binder;
  @Nonnull
  private final ParticipantDto participant;

  public ParticipantsDetail(@Nonnull final ParticipantService participantService) {
    this.participantService = participantService;
    this.binder = new Binder<>(ParticipantDto.class);
    this.participant = new ParticipantDto();


    init();
  }

  private void init() {
    final var saveButton = new Button("Save");

    final var nameLayout = new HorizontalLayout();
    nameLayout.setWidthFull();
    nameLayout.add(createTextField(
        ParticipantDto::getFirstName,
        ParticipantDto::setFirstName,
        Optional.of("First Name is required"),
        "First Name"
    ));
    nameLayout.add(createTextField(
        ParticipantDto::getLastName,
        ParticipantDto::setLastName,
        Optional.of("Last Name is required"),
        "Last Name"
    ));

    final var participantLayout = new VerticalLayout();
    participantLayout.setWidthFull();
    participantLayout.add(nameLayout);

    add(new ViewToolbar("Participant Detail", saveButton));
    add(participantLayout);
  }

  @Nonnull
  private TextField createTextField(@Nonnull final ValueProvider<ParticipantDto, String> valueProvider,
                                    @Nonnull final Setter<ParticipantDto, String> valueSetter,
                                    @Nonnull final Optional<String> required,
                                    @Nonnull final String label) {
    final var textField = new TextField(label);
    textField.setWidthFull();

    if (required.isPresent()) {
      binder.forField(textField)
          .asRequired(required.get())
          .bind(valueProvider, valueSetter);
    } else {
      binder.forField(textField)
          .bind(valueProvider, valueSetter);
    }

    return textField;
  }

}
