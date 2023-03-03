package chesspuzz.staff;

import chesspuzz.model.MoveTD;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.ArrayList;

public class TableNav {

    private final TableView<MoveTD> tableView;
    private final TableColumn<MoveTD, String> moveWhite;
    private final TableColumn<MoveTD, String> moveBlack;

    private final ObservableList<MoveTD> moveList;

    private boolean highlighting = false;
    private TableMoveSelection tableMoveSelection;

    public TableNav(TableView<MoveTD> tableView) {
        moveList = FXCollections.observableArrayList();

        this.tableView = tableView;
        this.tableView.setItems(moveList);
        this.tableView.getSelectionModel().setCellSelectionEnabled(true);

        this.tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //highlighting = false;

                Platform.runLater(() -> tableView.getParent().requestFocus());
            }
        });

        ObservableList<TableColumn<MoveTD, ?>> columns = tableView.getColumns();
        TableColumn<MoveTD, String> moveIndex = (TableColumn<MoveTD, String>) columns.get(0);
        moveWhite = (TableColumn<MoveTD, String>) columns.get(1);
        moveBlack = (TableColumn<MoveTD, String>) columns.get(2);

        // add column values to the table
        moveIndex.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().moveIndex)));
        moveWhite.setCellValueFactory(new PropertyValueFactory("moveWhite"));
        moveBlack.setCellValueFactory(new PropertyValueFactory("moveBlack"));

        moveIndex.setCellFactory(new Callback<TableColumn<MoveTD, String>, TableCell<MoveTD, String>>() {
            @Override
            public TableCell<MoveTD, String> call(TableColumn<MoveTD, String> column) {
                TableCell<MoveTD, String> cell = new TableCell<MoveTD, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                        }

                    }
                };

                cell.setDisable(true);
                return cell;
            }
        });

        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                if (highlighting) {
                    highlighting = false;
                    return;
                }

                if (selectedCells.size() < 1) return;

                int row = tableView.getSelectionModel().getSelectedIndex() + 1;
                int column = selectedCells.get(0).getColumn();
                int adjustment = column < 2 ? -2 : -1;


                int moveIndex = (row * 2) + adjustment;
                if (tableMoveSelection != null) tableMoveSelection.onMoveSelected(moveIndex);
            }
        });
    }

    public void setMoveList(ArrayList<MoveTD> moveList) {
        this.moveList.clear();
        this.moveList.addAll(moveList);
    }

    public void highlightMove(int row, int who) {
        highlighting = true;

        TableColumn<MoveTD, String> column = who == 0 ? moveWhite : moveBlack;
        tableView.getSelectionModel().select(row, column);

        MoveTD moveTD = moveList.get(row);
        tableView.scrollTo(moveTD);
    }

    public void setTableMoveSelection(TableMoveSelection tableMoveSelection) {
        this.tableMoveSelection = tableMoveSelection;
    }

    public void clearSelection() {
        tableView.getSelectionModel().clearSelection();
    }

    public void addToList(MoveTD moveTD) {
        moveList.add(moveTD);
    }

    public interface TableMoveSelection {
        void onMoveSelected(int moveIndex);
    }

}
