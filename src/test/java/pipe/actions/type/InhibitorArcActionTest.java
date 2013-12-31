package pipe.actions.type;

import org.junit.Before;
import org.junit.Test;
import pipe.controllers.PetriNetController;
import pipe.gui.ApplicationSettings;
import pipe.gui.Constants;
import pipe.historyActions.HistoryManager;
import pipe.models.PetriNet;
import pipe.models.component.Place;
import pipe.models.component.Token;
import pipe.models.component.Transition;
import pipe.views.PipeApplicationView;

import static org.mockito.Mockito.*;

public class InhibitorArcActionTest {
    private InhibitorArcAction action;
    private PetriNetController mockController;
    private PetriNet mockNet;
    private HistoryManager mockHistory;
    private PipeApplicationView mockApplicationView;
    private Token activeToken;

    @Before
    public void setUp() {
        action = new InhibitorArcAction("Inhibitor Arc", Constants.INHIBARC, "Add an inhibitor arc", "H");
        mockController = mock(PetriNetController.class);
        mockNet = mock(PetriNet.class);
        when(mockController.getPetriNet()).thenReturn(mockNet);

        mockHistory = mock(HistoryManager.class);
        when(mockController.getHistoryManager()).thenReturn(mockHistory);


        activeToken = mock(Token.class);
        mockApplicationView = mock(PipeApplicationView.class);
        when(mockApplicationView.getSelectedTokenName()).thenReturn("Default");
        when(mockController.getToken("Default")).thenReturn(activeToken);
        ApplicationSettings.register(mockApplicationView);
    }

    @Test
    public void createsInhibitorArcIfClickedOnPlace() {
        when(mockController.isCurrentlyCreatingArc()).thenReturn(false);

        Place place = mock(Place.class);
        action.doConnectableAction(place, mockController);

        verify(mockController).startCreatingInhibitorArc(place, activeToken);
    }

    @Test
    public void doesNotCreateInhibitorArcIfClickedOnTransition() {
        when(mockController.isCurrentlyCreatingArc()).thenReturn(false);

        Transition transition = mock(Transition.class);
        action.doConnectableAction(transition, mockController);

        verify(mockController, never()).startCreatingInhibitorArc(transition, activeToken);
    }

    @Test
    public void finishesCreatingArc() {
        when(mockController.isCurrentlyCreatingArc()).thenReturn(true);

        Transition transition = mock(Transition.class);
        action.doConnectableAction(transition, mockController);

        verify(mockController).finishCreatingArc(transition);
    }

}
