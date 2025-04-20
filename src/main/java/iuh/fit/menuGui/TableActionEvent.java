package menuGui;

/**
 *
 * @author RAVEN
 */
public interface TableActionEvent {

    public void onPlus(int row);

    public void onDelete(int row);

    public void onMinus(int row);
}
