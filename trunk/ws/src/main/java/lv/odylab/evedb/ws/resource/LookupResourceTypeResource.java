package lv.odylab.evedb.ws.resource;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lv.odylab.evedb.ws.EveDbResource;
import lv.odylab.evedb.ws.EveDbWsFacade;
import lv.odylab.evedb.ws.ProvidesJson;
import lv.odylab.evedb.ws.ProvidesXml;

import java.io.PrintWriter;

@Singleton
public class LookupResourceTypeResource extends EveDbResource implements ProvidesJson, ProvidesXml {
    private static final long serialVersionUID = 7684537824710167237L;

    private final EveDbWsFacade wsFacade;

    @Inject
    public LookupResourceTypeResource(EveDbWsFacade wsFacade) {
        this.wsFacade = wsFacade;
    }

    @Override
    public void provideJson(String query, PrintWriter writer) {
        writer.write(wsFacade.findResourceTypeByPartialTypeNameAsJson(query));
    }

    @Override
    public void provideXml(String query, PrintWriter writer) {
        writer.write(wsFacade.findResourceTypeByPartialTypeNameAsXml(query));
    }
}