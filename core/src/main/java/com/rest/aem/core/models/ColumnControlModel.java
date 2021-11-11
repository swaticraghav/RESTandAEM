package com.rest.aem.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ColumnControlModel.
 */
@Model(adaptables = Resource.class)
public class ColumnControlModel {

    private static final Logger log = LoggerFactory.getLogger(ColumnControlModel.class);

    @ValueMapValue
    @Optional
    private String[] columnLayout;
    
    @ValueMapValue
    @Optional
    private String columnWidths;

    public String getColumnWidths() {
		return columnWidths;
	}

	@ValueMapValue
    @Optional
    private String[] backgroundcolor;

    @ValueMapValue
    @Optional
    private String[] containerbackgroundcolor;

    private List<ColumnControlPojo> columnControl;
    private String backgroundclass;
    private String containerbackgroundclass;

    @PostConstruct
    protected void init() {
        columnControl = new ArrayList<>();
        if (null != columnLayout) {
            for (String columnLayoutvalues : columnLayout) {
                Gson gson = new Gson();
                ColumnControlPojo fromJson = gson.fromJson(columnLayoutvalues, ColumnControlPojo.class);
                columnControl.add(fromJson);
            }
        }
        if (backgroundcolor != null) {
            backgroundclass = getTagName(backgroundcolor);

        }
        if (containerbackgroundcolor != null) {
            containerbackgroundclass = getTagName(containerbackgroundcolor);

        }

    }

    private String getTagName(String[] backgroundcolor) {
        List<String> backgroundclasslist = new ArrayList<>();
        try (ResourceResolver resolver = JcrUtilService.getResourceResolver()) {
            TagManager tagmanager = (null != resolver) ? resolver.adaptTo(TagManager.class) : null;
            if (tagmanager != null) {
                for (String backgroundcolorclass : backgroundcolor) {
                    Tag productTag = tagmanager.resolve(backgroundcolorclass);
                    if (null != productTag) {
                        backgroundclasslist.add(productTag.getName());
                    }
                }
            }
        } catch (Exception e) {
            log.error("ColumnControlModel :: Exception", e);
        }

        return StringUtils.join(backgroundclasslist, " ");
    }

    public List<ColumnControlPojo> getColumnControl() {
        return columnControl;
    }

    public String getBackgroundclass() {
        return backgroundclass;
    }

    public String getContainerbackgroundclass() {
        return containerbackgroundclass;
    }

    public String getCarouselId() {
        return "myCarousel" + new SecureRandom().nextInt(999999999);
    }

}
