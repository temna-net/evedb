package lv.odylab.evedb.ws.resource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lv.odylab.evedb.ws.EveDbWsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class BaseMaterialsForTypeNameResource extends HttpServlet {
    private static final long serialVersionUID = -4921248984923260748L;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final EveDbWsFacade eveDbWsFacade;

    @Inject
    public BaseMaterialsForTypeNameResource(EveDbWsFacade eveDbWsFacade) {
        this.eveDbWsFacade = eveDbWsFacade;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json; charset=UTF-8");
            String typeName = req.getPathInfo().substring(1);
            String jsonString = eveDbWsFacade.getInvTypeMaterialsForTypeName(typeName);
            resp.getWriter().write(jsonString);
        } catch (Exception e) {
            logger.error("Application threw exception", e);
            resp.sendError(400);
        }
    }
}