import urllib.request
from PIL import Image

def get_album_art(album_name, album_img_url_dict, album_cover_path):

    print(f"Downloading album cover for {album_name}")

    # Get album cover
    url = album_img_url_dict[album_name]
    image = Image.open(urllib.request.urlopen(url))

    #image.save(f"{album_cover_path}/{album_name}_art.png")

    return image