package org.alexdev.icarus.messages.outgoing.catalogue;

import org.alexdev.icarus.game.catalogue.CatalogueItem;
import org.alexdev.icarus.game.catalogue.CataloguePage;
import org.alexdev.icarus.messages.headers.Outgoing;
import org.alexdev.icarus.messages.parsers.OutgoingMessageComposer;

public class CataloguePageMessageComposer extends OutgoingMessageComposer {

    private CataloguePage page;
    private String type;

    public CataloguePageMessageComposer(CataloguePage page, String type) {
        this.page = page;
        this.type = type;
    }

    @Override
    public void write() {

        response.init(Outgoing.CataloguePageMessageComposer);
        response.writeInt(this.page.getId());
        response.writeString(this.type);
        response.writeString(page.getLayout());
        
        response.writeInt(page.getImages().size());
        for (String image : page.getImages()) {
            response.writeString(image);
        }
        
       response.writeInt(page.getTexts().size());
        for (String text : page.getTexts()) {
            response.writeString(text);
        }

        response.writeInt(page.getItems().size()); 

        for (CatalogueItem item : page.getItems()) {
            item.serialise(response);
        }

        response.writeInt(0);
        response.writeBool(false);
        
        if (page.getLayout().equals("frontpage4")) {

            response.writeInt(4);

            response.writeInt(1);
            response.writeString("New Fitness Duck Bundle!");
            response.writeString("catalogue/feature_cata_vert_mallbundle3.png");
            response.writeInt(0);
            response.writeString("lympix16shop");
            response.writeInt(-1);

            response.writeInt(2);
            response.writeString("Clothes Shop");
            response.writeString("catalogue/feature_cata_hort_clothes.png");
            response.writeInt(0);
            response.writeString("clothing_top_picks");
            response.writeInt(-1);

            response.writeInt(3);
            response.writeString("The Icarus Pet Shop");
            response.writeString("catalogue/feature_cata_hort_pets.png");
            response.writeInt(0);
            response.writeString("pets_info");
            response.writeInt(-1);

            response.writeInt(4);
            response.writeString("Become a HC Member");
            response.writeString("catalogue/feature_cata_hort_HC_b.png");
            response.writeInt(0);
            response.writeString("hc_membership");
            response.writeInt(-1);

        }
        else {
            response.writeInt(0);
        }
        
        response.writeBool(false);

    }

}
