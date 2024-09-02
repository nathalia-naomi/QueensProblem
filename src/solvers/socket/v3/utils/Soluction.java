package solvers.socket.v3.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Soluction implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<int[][]> soluctions = new ArrayList<>();
    public long startTime;
    public long endTime;
    public int clientId;
}
