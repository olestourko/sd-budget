package com.olestourko.sdbudget.desktop.models;

import java.math.BigDecimal;
import com.olestourko.sdbudget.desktop.models.BudgetItemViewModel;
import com.olestourko.sdbudget.core.models.Month;
import java.util.Calendar;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 *
 * @author oles
 */
public class MonthViewModel implements IPeriod {

    /*
    An extractor is used to detect changes within list items (instead of just detecting added/removed items from the observable list)
    https://gist.github.com/andytill/3116203
    http://docs.oracle.com/javase/8/javafx/api/javafx/collections/FXCollections.html#observableArrayList-javafx.util.Callback-
     */
    private final Callback<BudgetItemViewModel, Observable[]> extractor = new Callback<BudgetItemViewModel, Observable[]>() {
        @Override
        public Observable[] call(BudgetItemViewModel item) {
            return new Observable[]{
                item.nameProperty(), item.amountProperty()
            };
        }
    };

    private Month model = new Month();

    protected final ObservableList<BudgetItemViewModel> revenues = FXCollections.observableArrayList(extractor);
    protected final ObservableList<BudgetItemViewModel> expenses = FXCollections.observableArrayList(extractor);
    protected final ObservableList<BudgetItemViewModel> adjustments = FXCollections.observableArrayList(extractor);

    public final SimpleObjectProperty<BudgetItemViewModel> netIncomeTarget = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Net Income Target", BigDecimal.ZERO));
    public final SimpleObjectProperty<BudgetItemViewModel> openingBalance = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Opening Balance", BigDecimal.ZERO));
    public final SimpleObjectProperty<BudgetItemViewModel> closingBalance = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Closing Balance", BigDecimal.ZERO));
    public final SimpleObjectProperty<BudgetItemViewModel> openingSurplus = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Opening Surplus", BigDecimal.ZERO));
    public final SimpleObjectProperty<BudgetItemViewModel> closingSurplus = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Closing Surplus", BigDecimal.ZERO));
    public final SimpleObjectProperty<BudgetItemViewModel> closingBalanceTarget = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Closing Balance Target", BigDecimal.ZERO));
    public final SimpleObjectProperty<BudgetItemViewModel> estimatedClosingBalance = new SimpleObjectProperty<BudgetItemViewModel>(new BudgetItemViewModel("Closing Balance (Estimated)", BigDecimal.ZERO));

    final public Calendar calendar;

    private final SimpleBooleanProperty isClosed = new SimpleBooleanProperty();

    public MonthViewModel(Calendar calendar) {
        this.calendar = calendar;
    }

    public Month getModel() {
        return model;
    }

    public void setModel(Month model) {
        this.model = model;
    }

    @Override
    public BigDecimal getTotalRevenues() {
        return revenues.stream().map(BudgetItemViewModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalExpenses() {
        return expenses.stream().map(BudgetItemViewModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalAdjustments() {
        return adjustments.stream().map(BudgetItemViewModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public ObservableList<BudgetItemViewModel> getRevenues() {
        return revenues;
    }

    @Override
    public ObservableList<BudgetItemViewModel> getExpenses() {
        return expenses;
    }

    @Override
    public ObservableList<BudgetItemViewModel> getAdjustments() {
        return adjustments;
    }

    @Override
    public void removeRevenue(BudgetItemViewModel item) {
        revenues.remove(item);
    }

    @Override
    public void removeExpense(BudgetItemViewModel item) {
        expenses.remove(item);
    }

    @Override
    public void removeAdjustment(BudgetItemViewModel item) {
        adjustments.remove(item);
    }

    @Override
    public void addRevenue(BudgetItemViewModel item) {
        revenues.add(item);
    }

    @Override
    public void addExpense(BudgetItemViewModel item) {
        expenses.add(item);
    }

    @Override
    public void addAdjustment(BudgetItemViewModel item) {
        adjustments.add(item);
    }

    // Properties
    public void setIsClosed(boolean value) {
        this.isClosed.set(value);
    }

    public boolean getIsClosed() {
        return this.isClosed.getValue();
    }

    public SimpleBooleanProperty isClosedProperty() {
        return this.isClosed;
    }

    @Override
    public BudgetItemViewModel getNetIncomeTarget() {
        return netIncomeTarget.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getNetIncomeTargetProperty() {
        return netIncomeTarget;
    }

    @Override
    public BudgetItemViewModel getOpeningBalance() {
        return openingBalance.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getOpeningBalanceProperty() {
        return openingBalance;
    }

    @Override
    public BudgetItemViewModel getClosingBalance() {
        return closingBalance.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getClosingBalanceProperty() {
        return closingBalance;
    }

    // Returns closing balance based on closed status
    public BudgetItemViewModel getFinalClosingBalance() {
        return getIsClosed() ? getClosingBalance() : getClosingBalanceTarget();
    }

    @Override
    public BudgetItemViewModel getOpeningSurplus() {
        return openingSurplus.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getOpeningSurplusProperty() {
        return openingSurplus;
    }

    @Override
    public BudgetItemViewModel getClosingSurplus() {
        return closingSurplus.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getClosingSurplusProperty() {
        return closingSurplus;
    }

    @Override
    public BudgetItemViewModel getClosingBalanceTarget() {
        return closingBalanceTarget.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getClosingBalanceTargetProperty() {
        return closingBalanceTarget;
    }

    @Override
    public BudgetItemViewModel getEstimatedClosingBalance() {
        return estimatedClosingBalance.getValue();
    }

    public SimpleObjectProperty<BudgetItemViewModel> getEstimatedClosingBalanceProperty() {
        return estimatedClosingBalance;
    }
}
