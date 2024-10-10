package com.onlinemarket.OnlinemarketProjectBackend.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;

@Service
@Transactional
public class CategoryService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 1;
    
    @Autowired
    private CategoryRepository repo;

    public List<Category> listByPage(CategoryPageInfo pageInfo,int pageNum,String sortDir, String keyword){
        Sort sort = Sort.by("name");
        
        if (sortDir.equals("asc")){
            sort = sort.ascending();
        } else if (sortDir.equals("desc")){
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum-1, ROOT_CATEGORIES_PER_PAGE, sort);
        Page<Category> pageCategories = null;

        if(keyword != null && !keyword.isEmpty()){
            pageCategories = repo.search(keyword, pageable);
        } else {
            pageCategories = repo.findRootCategories(pageable);
        }
        List<Category> rootCategories = pageCategories.getContent();
        pageInfo.setTotalItems(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if(keyword != null && !keyword.isEmpty()){
            List<Category> searchCategories = pageCategories.getContent();
            for(Category category : searchCategories){
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchCategories;
        }else {
            return hierarchicalCategories(rootCategories, sortDir);
        }
    }

    private List<Category> hierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for(Category rootCategory: rootCategories){
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = sortedSubCategories(rootCategory.getChildren(), sortDir);

            for(Category child_category: children){
                    
                hierarchicalCategories.add(Category.copyFull(child_category,"--"+child_category.getName()));
                listSubHierarchicalCategories(hierarchicalCategories, child_category, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
        Category parent, int sublevel, String sortDir){
            Set<Category> children = sortedSubCategories(parent.getChildren(), sortDir);
            int level = sublevel+1;

            String name = "";
            for(Category child_category: children){
                for(int i=0;i<level;i++){
                    name+="--";
                }
                hierarchicalCategories.add(Category.copyFull(child_category, name+child_category.getName()));
                listSubHierarchicalCategories(hierarchicalCategories, child_category, level, sortDir);
            }
    }

    public Category save(Category category){
        return repo.save(category);
    }

    public List<Category> listCategoriesInForm(){
        
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> inDB = repo.findRootCategories(Sort.by("name").ascending());

        for(Category category : inDB){
            if (category.getParent() == null){
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortedSubCategories(category.getChildren());
                for(Category child_category: children){
                    
                    categoriesUsedInForm.add(Category.copyIdAndName("--"+child_category.getName(), child_category.getId()));
                    listChildren(categoriesUsedInForm, child_category, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int sublevel) {
        int level = sublevel+1;
        Set<Category> children = sortedSubCategories(parent.getChildren());
        String name = "";
        for(Category child_category: children){
            for(int i=0;i<level;i++){
                name+="--";
            }
            categoriesUsedInForm.add(Category.copyIdAndName(name + child_category.getName(), child_category.getId()));
            listChildren(categoriesUsedInForm, child_category, level);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException{
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e){
            throw new CategoryNotFoundException("No category with ID: "+id);
        }
    }

    public String checkUnique(Integer id, String name, String alias){
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = repo.findByName(name);
        if(isCreatingNew){
            if(categoryByName != null){
                return "DuplicateName";
            } else {
                Category bbyAlias = repo.findByAlias(alias);
                if(bbyAlias != null){
                    return "DuplicateAlias";
                }
            }
        } else {
            if(categoryByName != null && categoryByName.getId() != id){
                return "DuplicateName";
            } else {
                Category bbyAlias = repo.findByAlias(alias);
                if(bbyAlias != null && bbyAlias.getId() != id){
                    return "DuplicateAlias";
                }
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortedSubCategories(Set<Category> children){
        return sortedSubCategories(children, "asc");
    }

    private SortedSet<Category> sortedSubCategories(Set<Category> children, String sortDir){
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

            @Override
            public int compare(Category o1, Category o2) {
                if(sortDir.equals("asc")){
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getName().compareTo(o1.getName());
                }
                
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = repo.countById(id);

        if(countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find category with ID: "+id);
        }
        repo.deleteById(id);
    }
}
