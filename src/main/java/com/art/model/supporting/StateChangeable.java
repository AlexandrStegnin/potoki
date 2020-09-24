package com.art.model.supporting;

import com.art.model.supporting.enums.MoneyState;

/**
 * @author Alexandr Stegnin
 */

public interface StateChangeable {

    MoneyState getState();

    boolean isActive();

}
